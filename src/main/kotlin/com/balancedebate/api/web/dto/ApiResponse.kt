package com.balancedebate.api.web.dto

import org.springframework.http.HttpStatus

data class ApiResponse<T>(
    val statusCode: Int,
    val message: String? = null,
    val data: T? = null
) {

    companion object {
        fun success(): ApiResponse<Unit> {
            return ApiResponse(statusCode = HttpStatus.OK.value())
        }

        fun <T> success(data: T? = null): ApiResponse<T> {
            return ApiResponse(statusCode = HttpStatus.OK.value(), data = data)
        }

        fun failure(status: Int, message: String?): ApiResponse<Unit> {
            return ApiResponse(status, message)
        }
    }
}
