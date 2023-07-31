package com.example.helper.domen.repository

import com.example.helper.domen.models.ApiResponse
import com.example.helper.domen.models.Archive
import com.example.helper.domen.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun updateUserInfo(user:User): ApiResponse<User>
    suspend fun getUserInfo():ApiResponse<User>
    suspend fun getOtherUserByName(userName:String):ApiResponse<User>
    suspend fun getUserById(userId:String):ApiResponse<User>
    fun getAllUsers(): Flow<ApiResponse<List<User>>>

}