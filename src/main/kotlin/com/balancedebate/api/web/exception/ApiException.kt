package com.balancedebate.api.web.exception

import java.lang.RuntimeException

class ApiException(var errorReason: ErrorReason, message: String) : RuntimeException(message) {

    var code: String? = null

    constructor(errorReason: ErrorReason, message: String, code: String) : this(errorReason, message) {
        this.code = code
    }
}

enum class ErrorReason(
    val status: Int,
) {
    SIGNUP_FAILED(200),
    LOGIN_FAILED(200),
    UNAUTHENTICATED(200),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND_ENTITY(404),
    CONFLICT(409),
    UNKNOWN(500),
}
