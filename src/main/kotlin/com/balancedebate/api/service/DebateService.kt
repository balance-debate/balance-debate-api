package com.balancedebate.api.service

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
import jakarta.servlet.http.Cookie
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
) {

    @Transactional(readOnly = true)
    fun getDebates(pageable: Pageable): DebateSliceResponse {
        return DebateSliceResponse.from(debateRepository.findAll(pageable))
    }

    @Transactional(readOnly = true)
    fun getDebate(id: Long): DebateGetResponse? {
        return debateRepository.findById(id).map { DebateGetResponse.from(it) }
            .orElseThrow { ApiException(ErrorReason.NOT_FOUND_ENTITY, "Debate with id $id not found", "NOT_FOUND_DEBATE") }
    }

    // TODO: 리팩토링
    @Transactional
    fun voteOnDebate(
        debateId: Long,
        request: VoteRequest,
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse
    ) {
        val loginUser = httpSession.getAttribute(LoginAccountArgumentResolver.LOGIN_ATTRIBUTE_NAME)
        val voteTokenCookie = httpServletRequest.cookies?.firstOrNull { it.name == "vote-token" }?.value

        // 비회원이고 쿠키에 vote-token 이 없을 경우
        if (loginUser == null && voteTokenCookie.isNullOrEmpty()) {
            val voteTokenUuid = UUID.randomUUID().toString()
            val cookie = Cookie("vote-token", voteTokenUuid)
            cookie.path = "/"
            cookie.maxAge = 60 * 60 * 24 * 180
            httpServletResponse.addCookie(cookie)

            debateRepository.findById(debateId)
                .ifPresentOrElse(
                    { debate -> debate.add(Vote(debate = debate, accountId = voteTokenUuid, target = request.target)) },
                    { throw ApiException(ErrorReason.NOT_FOUND_ENTITY, "Debate with id $debateId not found", "NOT_FOUND_DEBATE") }
                )
            return
        }

        // 비회원이고 쿠키에 vote-token 이 있을 경우
        if (loginUser == null && !voteTokenCookie.isNullOrEmpty()) {
            voteRepository.findByDebateIdAndAccountId(
                debateId = debateId,
                accountId = voteTokenCookie
            )?.let {
                throw ApiException(ErrorReason.CONFLICT, "Already voted for debate with id $debateId", "ALREADY_VOTED")
            }

            debateRepository.findById(debateId)
                .ifPresentOrElse(
                    { debate -> debate.add(Vote(debate = debate, accountId = voteTokenCookie, target = request.target)) },
                    { throw ApiException(ErrorReason.NOT_FOUND_ENTITY, "Debate with id $debateId not found", "NOT_FOUND_DEBATE") }
                )
            return
        }

        /**
         * 회원일 경우)
         * 쿠키에 vote_token=uuid 가 없으면 debate.add(Vote()) 실행
         * 쿠키에 vote_token=uuid 가 있는데 해당 uuid 로 생성된 Vote 가 없으면, debate.add(Vote()) 실행
         * 쿠키에 vote_token=uuid 가 있는데 해당 uuid 로 생성된 Vote 가 있으면, 중복 투표인 것임
         */

        /**
         * + 따닥 방지 (낙관적 락)
         * + 성능 개선
         */
        debateRepository.findById(debateId)
            .ifPresentOrElse(
                { debate -> debate.add(Vote(debate = debate, accountId = UUID.randomUUID().toString(), target = request.target)) },
                { throw ApiException(ErrorReason.NOT_FOUND_ENTITY, "Debate with id $debateId not found", "NOT_FOUND_DEBATE") }
            )
    }

    @Transactional(readOnly = true)
    fun hasVote(debateId: Long, httpServletRequest: HttpServletRequest): HasVoteResponse {
        val loginUser = httpSession.getAttribute(LoginAccountArgumentResolver.LOGIN_ATTRIBUTE_NAME)
        val voteTokenCookie = httpServletRequest.cookies?.firstOrNull { it.name == "vote-token" }?.value

        if (loginUser == null && voteTokenCookie.isNullOrEmpty()) {
            return HasVoteResponse(false)
        }

        if (loginUser == null && !voteTokenCookie.isNullOrEmpty()) {
            val hasVote = voteRepository.findByDebateIdAndAccountId(debateId, voteTokenCookie) != null
            return HasVoteResponse(hasVote)
        }

        // TODO: 회원일 경우 작업
        return HasVoteResponse(false)
    }

    @Transactional(readOnly = true)
    fun getVoteResult(debateId: Long): VoteResultResponse {
        val debate = debateRepository.findById(debateId)
            .orElseThrow { ApiException(ErrorReason.NOT_FOUND_ENTITY, "Debate with id $debateId not found", "NOT_FOUND_DEBATE") }

        val votes = debate.votes
        val choiceACount = votes.count { it.target == VoteTarget.CHOICE_A }
        val choiceBCount = votes.count { it.target == VoteTarget.CHOICE_B }

        return VoteResultResponse(choiceACount, choiceBCount)
    }
}
