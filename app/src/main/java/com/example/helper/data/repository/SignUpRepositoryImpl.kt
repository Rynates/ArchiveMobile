package com.example.helper.data.repository

import com.example.helper.data.remote.KtorApi
import com.example.helper.domen.models.ApiResponse
import com.example.helper.domen.models.AuthRequest
import com.example.helper.domen.models.User
import com.example.helper.domen.repository.SignUpRepository
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val api: KtorApi):SignUpRepository {
    override suspend fun signUp(name: String, pass: String,username:String): ApiResponse<User> {
        return try {
            api.signUp(
                request = AuthRequest(
                    email = name,
                    password = pass,
                    username = username
                )
            )
            ApiResponse(success = true)
        }catch (e:Exception){
            ApiResponse(success = false, error = e.message)
        }
    }
}