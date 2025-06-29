package com.balancedebate.api.web.exception

import java.lang.RuntimeException

class ApiException(var errorReason: ErrorReason, message: String) : RuntimeException(message)

enum class ErrorReason(
    val status: Int,
) {
    SIGNUP_FAILED(200),
    LOGIN_FAILED(200),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND_ENTITY(404),
    UNKNOWN(500),
}
