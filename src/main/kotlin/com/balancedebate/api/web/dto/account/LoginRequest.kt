package com.balancedebate.api.web.dto.account

data class LoginRequest(
    val nickname: String,
    val password: String,
)
