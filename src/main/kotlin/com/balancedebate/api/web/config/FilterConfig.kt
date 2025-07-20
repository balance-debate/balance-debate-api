package com.balancedebate.api.web.config

import com.balancedebate.api.web.filter.SameSiteCookieFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    @Bean
    fun sameSiteCookieFilter(): FilterRegistrationBean<SameSiteCookieFilter> {
        val registration = FilterRegistrationBean(SameSiteCookieFilter())
        registration.order = Integer.MIN_VALUE
        return registration
    }
}