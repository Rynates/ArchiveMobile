package com.example.helper.domen.repository

import com.example.helper.domen.models.ApiResponse
import com.example.helper.domen.models.User

interface SignUpRepository {
    suspend fun signUp(email:String,password: String,username:String):ApiResponse<User>
}