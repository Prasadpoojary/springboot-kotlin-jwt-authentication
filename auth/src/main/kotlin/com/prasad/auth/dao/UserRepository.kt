package com.prasad.auth.dao

import com.prasad.auth.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User,Int>
{
    fun findByEmail(email:String): User?
}