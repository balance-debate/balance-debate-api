package com.balancedebate.api.web.controller

import com.balancedebate.api.domain.account.Account
import com.balancedebate.api.service.AccountService
import com.balancedebate.api.web.config.LoginAccount
import com.balancedebate.api.web.config.LoginAccountArgumentResolver
import com.balancedebate.api.web.dto.ApiResponse
import com.balancedebate.api.web.dto.account.AccountResponse
import com.balancedebate.api.web.dto.account.LoginRequest
import com.balancedebate.api.web.dto.account.SignupRequest
import jakarta.servlet.http.HttpSession
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AccountController(
    private val accountService: AccountService,
    private val httpSession: HttpSession,
) {

    @PostMapping("/signup")
    fun signup(@RequestBody request: SignupRequest): ApiResponse<Unit> {
        accountService.signUp(request)
        return ApiResponse.success()
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ApiResponse<Unit> {
        val account = accountService.login(request)
        httpSession.setAttribute(LoginAccountArgumentResolver.LOGIN_ATTRIBUTE_NAME, account)
        return ApiResponse.success()
    }

    @PostMapping("/logout")
    fun logout(@LoginAccount account: Account): ApiResponse<Unit> {
        httpSession.removeAttribute(LoginAccountArgumentResolver.LOGIN_ATTRIBUTE_NAME)
        return ApiResponse.success()
    }

    @GetMapping("/me")
    fun me(): ApiResponse<AccountResponse> {
        val account = accountService.getMe()
        return ApiResponse.success(AccountResponse(account.nickname, account.profileEmoji))
    }
}
