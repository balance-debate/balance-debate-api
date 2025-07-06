package com.balancedebate.api.service

import com.balancedebate.api.domain.account.DebateRepository
import com.balancedebate.api.web.dto.debate.DebateGetResponse
import com.balancedebate.api.web.dto.debate.DebateSliceResponse
import com.balancedebate.api.web.exception.ApiException
import com.balancedebate.api.web.exception.ErrorReason
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
}
