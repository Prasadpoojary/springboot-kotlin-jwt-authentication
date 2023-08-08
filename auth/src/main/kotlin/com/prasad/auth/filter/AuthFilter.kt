package com.prasad.auth.filter

import com.prasad.auth.config.UserInfoService
import com.prasad.auth.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class AuthFilter(private val jwtService:JwtService, private val userInfoService: UserInfoService): OncePerRequestFilter()
{
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain )
    {
        var token:String?=request.getHeader("Authorization")
        if(token?.startsWith("Bearer ")==true)
        {

            token= token.substring(7)
            val email=jwtService.extractUsername(token)
            val userDetails:UserDetails?=userInfoService.loadUserByUsername(email)

            if((userDetails != null) && jwtService.validateToken(token,userDetails) && (SecurityContextHolder.getContext().authentication == null))
            {
                val authenticationToken=UsernamePasswordAuthenticationToken(userDetails,null,userDetails.authorities)
                authenticationToken.details=WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication=authenticationToken
            }
        }

        filterChain.doFilter(request,response)
    }

}