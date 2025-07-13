package com.balancedebate.api.web.controller

import com.balancedebate.api.service.DebateService
import com.balancedebate.api.web.dto.ApiResponse
import com.balancedebate.api.web.dto.account.VoteRequest
import com.balancedebate.api.web.dto.account.VoteResultResponse
import com.balancedebate.api.web.dto.debate.DebateGetResponse
import com.balancedebate.api.web.dto.debate.DebateSliceResponse
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@RestController
class DebateController(
    private val debateService: DebateService,
) {

    @GetMapping("/debates")
    fun getDebates(@RequestParam page: Int, @RequestParam size: Int): ApiResponse<DebateSliceResponse> {
        return ApiResponse.success(debateService.getDebates(PageRequest.of(page, size)))
    }

    @GetMapping("/debates/{id}")
    fun getDebate(@PathVariable id: Long): ApiResponse<DebateGetResponse> {
        return ApiResponse.success(debateService.getDebate(id))
    }

    @PostMapping("/debates/{debateId}/votes")
    fun voteOnDebate(@PathVariable debateId: Long, @RequestBody request: VoteRequest): ApiResponse<Unit> {
        debateService.voteOnDebate(debateId, request)
        return ApiResponse.success()
    }

    @GetMapping("/debates/{debateId}/votes")
    fun getVoteResult(@PathVariable debateId: Long): ApiResponse<VoteResultResponse> {
        return ApiResponse.success(debateService.getVoteResult(debateId))
    }
}
