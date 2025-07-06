package com.balancedebate.api.service

import com.balancedebate.api.domain.account.Account
import com.balancedebate.api.domain.account.AccountRepository
import com.balancedebate.api.web.dto.account.LoginRequest
import com.balancedebate.api.web.dto.account.SignupRequest
import com.balancedebate.api.web.exception.ApiException
import com.balancedebate.api.web.exception.ErrorReason
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {

    companion object {
        private val EMOJIS = listOf(
            "😀", "🤗", "😍", "🤩", "🥸", "😎", "🤠", "😇", "🥳", "🤓",
            "🐶", "🐱", "🐵", "🐹", "🐰", "🦊", "🐻", "🐯", "🦁", "🐷"
        )
    }

    @Transactional
    fun signUp(request: SignupRequest) {
        val foundAccount = accountRepository.findByNickname(request.nickname)
        if (foundAccount.isPresent) {
            throw ApiException(ErrorReason.SIGNUP_FAILED, "이미 존재하는 계정입니다.", "ALREADY_SIGNED_UP_ACCOUNT")
        }

        val account = Account.of(request.nickname, request.password, EMOJIS.random())
        accountRepository.save(account)
    }

    @Transactional
    fun login(request: LoginRequest): Account {
        val account = accountRepository.findByNickname(request.nickname)
            .orElseThrow { ApiException(ErrorReason.LOGIN_FAILED, "존재하지 않는 계정입니다.", "NOT_FOUND_ACCOUNT") }

        if (!account.isCorrectPassword(request.password)) {
            throw ApiException(ErrorReason.LOGIN_FAILED, "비밀번호가 일치하지 않습니다.", "INCORRECT_PASSWORD")
        }

        return account
    }
}
