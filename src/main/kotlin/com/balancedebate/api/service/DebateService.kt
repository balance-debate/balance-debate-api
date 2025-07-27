package com.balancedebate.api.service

import com.balancedebate.api.domain.debate.DebateRepository
import com.balancedebate.api.domain.debate.Vote
import com.balancedebate.api.domain.debate.VoteTarget
import com.balancedebate.api.web.dto.account.VoteRequest
import com.balancedebate.api.web.dto.account.VoteResultResponse
import com.balancedebate.api.web.dto.debate.DebateGetResponse
import com.balancedebate.api.web.dto.debate.DebateSliceResponse
import com.balancedebate.api.web.exception.ApiException
import com.balancedebate.api.web.exception.ErrorReason
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DebateService(
    private val debateRepository: DebateRepository,
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

    @Transactional
    fun voteOnDebate(debateId: Long, request: VoteRequest) {
        // TODO: 중복 투표 검사, 비회원/회원 구분 처리, 성능 개선
        debateRepository.findById(debateId)
            .ifPresentOrElse(
                { debate -> debate.add(Vote(debate = debate, accountId = UUID.randomUUID().toString(), target = request.target)) },
                { throw ApiException(ErrorReason.NOT_FOUND_ENTITY, "Debate with id $debateId not found", "NOT_FOUND_DEBATE") }
            )
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
