package com.balancedebate.api.web.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.session.web.http.DefaultCookieSerializer

@Configuration
class HttpSessionConfig {

    @Bean
    fun cookieSerializer(): DefaultCookieSerializer {
        val serializer = DefaultCookieSerializer()
        serializer.setSameSite("None")
        serializer.setUseSecureCookie(true)
        return serializer
    }
}