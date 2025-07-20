package com.balancedebate.api.web.filter

import jakarta.servlet.*
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponseWrapper
import org.springframework.stereotype.Component

@Component
class SameSiteCookieFilter : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val wrappedResponse = object : HttpServletResponseWrapper(response as HttpServletResponse) {

            override fun addHeader(name: String, value: String) {
                if (name.equals("Set-Cookie", ignoreCase = true) && value.startsWith("JSESSIONID")) {
                    val updatedValue = value
                        .plus(if (!value.contains("SameSite")) "; SameSite=None" else "")
                        .plus(if (!value.contains("Secure")) "; Secure" else "")
                    super.addHeader(name, updatedValue)
                } else {
                    super.addHeader(name, value)
                }
            }
        }

        chain.doFilter(request, wrappedResponse)
    }
}
