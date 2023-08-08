package com.prasad.auth.config

import com.prasad.auth.filter.AuthFilter
import jakarta.servlet.Filter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer

@EnableWebSecurity
@Configuration
class SecurityConfig(private val authFilter:AuthFilter)
{
    @Bean
    fun  userDetailService():UserDetailsService
    {
        return UserInfoService()
    }

    @Bean
    fun passwordEncoder():PasswordEncoder
    {
        return BCryptPasswordEncoder()
    }


    @Bean
    fun securityFilterChain(http:HttpSecurity):SecurityFilterChain
    {
        return http.csrf().disable()
            .authorizeHttpRequests { auth ->
                    auth.requestMatchers("/auth/hello","/auth/add","/auth/authenticate").permitAll()
                    auth.requestMatchers("/auth/**").authenticated()
            }
            .sessionManagement{session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun authenticationProvider():AuthenticationProvider
    {
        val daoAuthenticationProvider= DaoAuthenticationProvider()
        daoAuthenticationProvider.setUserDetailsService(userDetailService())
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder())
        return daoAuthenticationProvider
    }

    @Bean
    fun authenticationManager(authConfig:AuthenticationConfiguration):AuthenticationManager
    {
        return authConfig.authenticationManager
    }

}