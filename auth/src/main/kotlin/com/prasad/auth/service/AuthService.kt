package com.prasad.auth.service

import com.prasad.auth.dao.UserRepository
import com.prasad.auth.model.User
import org.springframework.stereotype.Service

@Service
class AuthService(private val userRepository: UserRepository) {

    fun findUserByEmail(email: String): User?
    {
        return userRepository.findByEmail(email)
    }

    fun register(user:User):User
    {
        return userRepository.save(user)
    }

    fun allUser():List<User>
    {
        return userRepository.findAll()
    }

    fun fetchUser(id:Int):User
    {
        return userRepository.findById(id).get()
    }

    fun deleteUse(id:Int):Boolean
    {
        return try
        {
            userRepository.deleteById(id)
            true
        }
        catch(e:Exception)
        {
            println(e.message)
            false
        }
    }

}