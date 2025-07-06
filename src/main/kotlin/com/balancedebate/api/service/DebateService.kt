package com.balancedebate.api.service

import com.balancedebate.api.domain.account.DebateRepository
import com.balancedebate.api.web.dto.debate.DebateSliceResponse
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
}
