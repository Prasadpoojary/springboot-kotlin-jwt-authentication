package com.prasad.auth.service

import com.prasad.auth.config.UserInfoDetail
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.CryptoPrimitive
import java.security.Key
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.sign
import kotlin.reflect.typeOf

@Service
class JwtService
{

    private val SECRET:String ="5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437"

    fun generateToken(username:String):String
    {
        val claims:Map<String,Any> = HashMap()
        return Jwts.builder()
            .setClaims(claims)
            .signWith(signInKey())
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis()+1000*60*30))
            .compact()
    }

    fun validateToken(token:String,userDetails:UserDetails):Boolean
    {
        return extractUsername(token).lowercase().equals(userDetails.username.lowercase()) && !extractExpiry(token).before(Date())
    }

    fun extractUsername(token:String): String
    {
        val claims:Claims=extractClaims(token)
        return claims.subject
    }

    private fun extractExpiry(token:String): Date
    {
        val claims:Claims=extractClaims(token)
        return claims.expiration
    }


    private fun extractClaims(token:String):Claims
    {
        return Jwts.parserBuilder()
            .setSigningKey(signInKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun signInKey():Key
    {
        val keyBytes: ByteArray = Decoders.BASE64.decode(SECRET)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}

