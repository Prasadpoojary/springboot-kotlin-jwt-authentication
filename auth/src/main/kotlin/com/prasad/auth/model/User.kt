package com.prasad.auth.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name="user")
data class User(@Id @GeneratedValue(strategy = GenerationType.AUTO) val id:Int, val name:String, val email:String, val password:String, val role:String)
