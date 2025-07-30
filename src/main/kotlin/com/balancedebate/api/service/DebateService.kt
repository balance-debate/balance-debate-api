package com.balancedebate.api.service

import com.balancedebate.api.domain.account.Account
import com.balancedebate.api.domain.account.AccountRepository
import com.balancedebate.api.domain.debate.DebateRepository
import com.balancedebate.api.domain.debate.Vote
import com.balancedebate.api.domain.debate.VoteRepository
import com.balancedebate.api.domain.debate.VoteTarget
import com.balancedebate.api.web.config.LoginAccountArgumentResolver
import com.balancedebate.api.web.dto.account.HasVoteResponse
import com.balancedebate.api.web.dto.account.VoteRequest
import com.balancedebate.api.web.dto.account.VoteResultResponse
import com.balancedebate.api.web.dto.debate.DebateGetResponse
import com.balancedebate.api.web.dto.debate.DebateSliceResponse
import com.balancedebate.api.web.exception.ApiException
import com.balancedebate.api.web.exception.ErrorReason
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class DebateService(
    private val debateRepository: DebateRepository,
    private val voteRepository: VoteRepository,
    private val httpSession: HttpSession,
    private val accountRepository: AccountRepository,
) {

    companion object {
        private const val VOTE_TOKEN_COOKIE_NAME = "vote-token"

        private const val VOTE_TOKEN_COOKIE_MAX_AGE = 60 * 60 * 24 * 180 // 180 days
    }

    @Transactional(readOnly = true)
    fun getDebates(pageable: Pageable): DebateSliceResponse {
        return DebateSliceResponse.from(debateRepository.findAll(pageable))
    }

    @Transactional(readOnly = true)
    fun getDebate(id: Long): DebateGetResponse? {
        return debateRepository.findById(id).map { DebateGetResponse.from(it) }
            .orElseThrow { ApiException(ErrorReason.NOT_FOUND_ENTITY, "Debate with id $id not found", "NOT_FOUND_DEBATE") }
    }

    @Transactional
    fun voteOnDebate(debateId: Long, request: VoteRequest, httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse) {
        val loginUser = httpSession.getAttribute(LoginAccountArgumentResolver.LOGIN_ATTRIBUTE_NAME)
        val voteTokenCookie = httpServletRequest.cookies?.firstOrNull { VOTE_TOKEN_COOKIE_NAME == it.name }?.value
        val debate = debateRepository.findById(debateId)
            .orElseThrow { ApiException(ErrorReason.NOT_FOUND_ENTITY, "Debate with id $debateId not found", "NOT_FOUND_DEBATE") }

        if (loginUser == null) {
            // 비회원이고 쿠키에 vote-token 이 없을 경우
            if (voteTokenCookie.isNullOrEmpty()) {
                val voteTokenUuid = createVoteTokenAndSetCookie(httpServletResponse)
                debate.add(Vote(debate = debate, uuid = voteTokenUuid, accountId = null, target = request.target))
                return
            }

            // 비회원이고 쿠키에 vote-token 이 있을 경우
            if (voteTokenCookie.isNotEmpty()) {
                voteRepository.findByDebateIdAndUuid(debateId = debateId, uuid = voteTokenCookie)?.let {
                    throw ApiException(ErrorReason.CONFLICT, "Already voted for debate with id $debateId", "ALREADY_VOTED")
                }

                debate.add(Vote(debate = debate, uuid = voteTokenCookie, accountId = null, target = request.target))
                return
            }
        } else {
            val account = loginUser as Account
            val accountUser = accountRepository.findByNickname(account.nickname).get()

            // 해당 계정으로 이미 투표한 경우
            voteRepository.findByDebateIdAndAccountId(debateId = debateId, accountId = accountUser.id!!)?.let {
                throw ApiException(ErrorReason.CONFLICT, "Already voted for debate with id $debateId", "ALREADY_VOTED")
            }

            // 회원이고 쿠키에 vote-token 이 없을 경우
            if (voteTokenCookie.isNullOrEmpty()) {
                val voteTokenUuid = createVoteTokenAndSetCookie(httpServletResponse)
                debate.add(Vote(debate = debate, uuid = voteTokenUuid, accountId = accountUser.id, target = request.target))
                return
            }

            // 회원이고 쿠키에 vote-token 이 있을 경우
            if (voteTokenCookie.isNotEmpty()) {
                voteRepository.findByDebateIdAndUuid(debateId = debateId, uuid = voteTokenCookie)?.let {
                    throw ApiException(ErrorReason.CONFLICT, "Already voted for debate with id $debateId", "ALREADY_VOTED")
                }
                debate.add(Vote(debate = debate, uuid = voteTokenCookie, accountId = accountUser.id, target = request.target))
                return
            }
        }
    }

    private fun createVoteTokenAndSetCookie(httpServletResponse: HttpServletResponse): String {
        val voteTokenUuid = UUID.randomUUID().toString()
        httpServletResponse.setHeader(
            "Set-Cookie",
            "$VOTE_TOKEN_COOKIE_NAME=$voteTokenUuid; Path=/; HttpOnly; Secure; SameSite=None"
        )
        return voteTokenUuid
    }

    @Transactional(readOnly = true)
    fun hasVote(debateId: Long, httpServletRequest: HttpServletRequest): HasVoteResponse {
        val loginUser = httpSession.getAttribute(LoginAccountArgumentResolver.LOGIN_ATTRIBUTE_NAME)
        val voteTokenCookie = httpServletRequest.cookies?.firstOrNull { it.name == VOTE_TOKEN_COOKIE_NAME }?.value

        return if (!voteTokenCookie.isNullOrEmpty()) {
            val hasVote = voteRepository.findByDebateIdAndUuid(debateId, voteTokenCookie) != null
            HasVoteResponse(hasVote)
        } else if (loginUser == null && voteTokenCookie.isNullOrEmpty()) {
            return HasVoteResponse(false)
        } else {
            val account = loginUser as Account
            val accountUser = accountRepository.findByNickname(account.nickname).get()
            val hasVote = voteRepository.findByDebateIdAndAccountId(debateId, accountUser.id!!) != null
            HasVoteResponse(hasVote)
        }
    }

    @Transactional(readOnly = true)
    fun getVoteResult(debateId: Long, httpServletRequest: HttpServletRequest): VoteResultResponse {
        val debate = debateRepository.findById(debateId)
            .orElseThrow { ApiException(ErrorReason.NOT_FOUND_ENTITY, "Debate with id $debateId not found", "NOT_FOUND_DEBATE") }

        val hasVote = hasVote(debateId, httpServletRequest)
        if (!hasVote.hasVote) {
            throw ApiException(ErrorReason.FORBIDDEN, "You have not voted for this debate", "NOT_VOTED")
        }

        val votes = debate.votes
        val choiceACount = votes.count { it.target == VoteTarget.CHOICE_A }
        val choiceBCount = votes.count { it.target == VoteTarget.CHOICE_B }

        return VoteResultResponse(choiceACount, choiceBCount)
    }
}
