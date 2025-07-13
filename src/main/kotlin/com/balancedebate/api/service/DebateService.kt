package com.balancedebate.api.service

import com.balancedebate.api.domain.account.AccountRepository
import com.balancedebate.api.domain.debate.DebateRepository
import com.balancedebate.api.domain.debate.Vote
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
    private val accountRepository: AccountRepository,
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
    fun vote(debateId: Long) {
        debateRepository.findById(debateId)
            .ifPresentOrElse(
                { debate -> debate.add(Vote(debate = debate, accountId = UUID.randomUUID().toString())) },
                { throw ApiException(ErrorReason.NOT_FOUND_ENTITY, "Debate with id $debateId not found", "NOT_FOUND_DEBATE") }
            )
    }
}
