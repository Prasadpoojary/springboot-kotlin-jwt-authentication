package com.prasad.auth.controller

import com.prasad.auth.constant.MsgStatus
import com.prasad.auth.dto.AuthRequest
import com.prasad.auth.dto.Message
import com.prasad.auth.dto.UserDTO
import com.prasad.auth.model.User
import com.prasad.auth.service.AuthService
import com.prasad.auth.service.JwtService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Objects

@RestController
@RequestMapping("/auth")
class AuthController(private val authService:AuthService,private val authenticationManager: AuthenticationManager, private val jwtService: JwtService)
{



    @GetMapping("/hello")
    fun sayHello():ResponseEntity<Message>
    {
        val message=Message(MsgStatus.SUCCESS,"Hello world...")
        return ResponseEntity.ok(message)
    }


    @PostMapping("/add")
    fun register(@RequestBody user:User):ResponseEntity<Any>
    {
        return try {
            val responseUser:User=authService.register(user)
            val response=UserDTO("Registered Successfully",responseUser.name,responseUser.email,responseUser.role)
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        }
        catch (e:Exception) {
            println(e.message)
            val message=Message(MsgStatus.FAILURE,"Unable to register")
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message)
        }

    }

    @GetMapping("/secure")
    fun secureTest():ResponseEntity<Message>
    {
        val message=Message(MsgStatus.SUCCESS,"Secured endpoint")
        return ResponseEntity.ok(message)
    }

    @GetMapping("/all")
    fun allUser():ResponseEntity<List<User>>
    {
        val userList:List<User> =authService.allUser()
        return ResponseEntity.ok(userList)
    }


    @GetMapping("/user/{id}")
    fun getUser(@PathVariable id:Int):ResponseEntity<User>
    {
        val user:User=authService.fetchUser(id)
        return ResponseEntity.ok(user)
    }


    @GetMapping("/delete/{id}")
    fun deleteUser(@PathVariable id:Int):ResponseEntity<Message>
    {
        val message:Message
        return if(authService.deleteUse(id)) {
            message=Message(MsgStatus.SUCCESS,"Deleted successfully")
            ResponseEntity.ok(message)
        } else {
            message=Message(MsgStatus.FAILURE,"Unable to delete")
            ResponseEntity.badRequest().body(message)
        }

    }




    @PostMapping("/authenticate")
    fun authenticate(@RequestBody authRequest:AuthRequest):ResponseEntity<Message>
    {
        var authentication:Authentication?=null

        try
        {
            authentication=this.authenticationManager.authenticate(UsernamePasswordAuthenticationToken(authRequest.email,authRequest.password))
        }
        catch (e:Exception)
        {
            println(e.message)
            val message=Message(MsgStatus.FAILURE,"Invalid Username or Password")
            return ResponseEntity.badRequest().body(message)
        }

        if(authentication!!.isAuthenticated)
        {
            val token:String=jwtService.generateToken(authRequest.email)
            val message=Message(MsgStatus.SUCCESS,token)
            return ResponseEntity.ok(message)
        }

        val message=Message(MsgStatus.FAILURE,"Invalid Username or Password")
        return ResponseEntity.badRequest().body(message)

    }
}