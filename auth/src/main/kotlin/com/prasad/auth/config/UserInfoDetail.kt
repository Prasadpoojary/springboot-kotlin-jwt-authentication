package com.prasad.auth.config

import com.prasad.auth.model.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserInfoDetail(user: User) :UserDetails
{
   var userEmail:String=""
   var userPassword:String=""
   var userAuthorities:Collection<GrantedAuthority>?=null

    init
    {
        userEmail=user.email
        userPassword=user.password
        userAuthorities=user.role.split(',').map{it:String->SimpleGrantedAuthority(it)}
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return userAuthorities as MutableCollection<out GrantedAuthority>
    }

    override fun getPassword(): String {

        return userPassword
    }

    override fun getUsername(): String {
        return userEmail
    }

    override fun isAccountNonExpired(): Boolean {
        return true;
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}