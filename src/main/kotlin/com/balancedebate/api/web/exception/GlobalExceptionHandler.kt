package com.balancedebate.api.web.exception

import com.balancedebate.api.web.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [ApiException::class])
    fun handleApiException(exception: ApiException): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity(
            ApiResponse.failure(exception.errorReason.status, exception.message, exception.code),
            HttpStatus.valueOf(exception.errorReason.status)
        )
    }

    @ExceptionHandler(value = [Exception::class])
    fun handleException(exception: Exception): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity(
            ApiResponse.failure(ErrorReason.UNKNOWN.status, exception.message),
            HttpStatus.INTERNAL_SERVER_ERROR,
        )
    }
}
