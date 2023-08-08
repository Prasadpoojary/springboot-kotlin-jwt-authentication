package com.prasad.auth.config

import com.prasad.auth.model.User
import com.prasad.auth.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserInfoService:UserDetailsService
{
    @Autowired
    private lateinit var authService: AuthService;


    override fun loadUserByUsername(email: String?): UserDetails {
        val user: User = authService.findUserByEmail(email!!)!!
        return UserInfoDetail(user)
    }
}