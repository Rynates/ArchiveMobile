package com.example.helper.domen.repository

import com.example.helper.domen.models.*



interface LoginRepository {
    suspend fun signIn(email: String?, password: String?): AuthResult<Unit>
    suspend fun authenticate():AuthResult<Unit>
    suspend fun signOut():AuthResult<Unit>

}