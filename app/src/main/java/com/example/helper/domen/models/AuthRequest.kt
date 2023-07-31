package com.example.helper.domen.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username:String?=null,
    val email:String,
    val password:String
)
