package com.balancedebate.api.web.config

import com.balancedebate.api.web.exception.ApiException
import com.balancedebate.api.web.exception.ErrorReason
import jakarta.servlet.http.HttpSession
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class LoginAccountArgumentResolver(private val httpSession: HttpSession) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginAccount::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any {
        return httpSession.getAttribute(LOGIN_ATTRIBUTE_NAME) ?: ApiException(ErrorReason.UNAUTHORIZED, "로그인이 필요합니다.")
    }

    companion object {
        const val LOGIN_ATTRIBUTE_NAME = "LOGIN_ACCOUNT"
    }
}
