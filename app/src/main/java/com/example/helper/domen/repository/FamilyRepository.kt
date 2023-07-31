package com.example.helper.domen.repository

import com.example.helper.domen.models.ApiResponse
import com.example.helper.domen.models.Document
import com.example.helper.domen.models.Relative
import com.example.helper.domen.models.RelativeResponce
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.flow.Flow

interface FamilyRepository {
    suspend fun addRelative(relative:Relative): ApiResponse<List<RelativeResponce>>
    suspend fun getRelatives():ApiResponse<List<RelativeResponce>>?
    suspend fun getRelativeById(relativeId:String):ApiResponse<RelativeResponce>?
    suspend fun updateRelative(relative: Relative):ApiResponse<RelativeResponce>?
    suspend fun deleteRelative(relativeId:String)
    suspend fun getAllRelatives(): ApiResponse<List<RelativeResponce>>
}