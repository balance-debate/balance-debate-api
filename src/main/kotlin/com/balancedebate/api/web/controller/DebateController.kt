package com.balancedebate.api.web.controller

import com.balancedebate.api.domain.account.Account
import com.balancedebate.api.service.AccountService
import com.balancedebate.api.service.DebateService
import com.balancedebate.api.web.config.LoginAccount
import com.balancedebate.api.web.config.LoginAccountArgumentResolver
import com.balancedebate.api.web.dto.ApiResponse
import com.balancedebate.api.web.dto.account.AccountResponse
import com.balancedebate.api.web.dto.account.LoginRequest
import com.balancedebate.api.web.dto.account.SignupRequest
import com.balancedebate.api.web.dto.debate.DebateGetResponse
import com.balancedebate.api.web.dto.debate.DebateSliceResponse
import jakarta.servlet.http.HttpSession
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
}
