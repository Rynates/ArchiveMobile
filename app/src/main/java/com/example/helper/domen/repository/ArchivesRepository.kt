package com.example.helper.domen.repository

import com.example.helper.domen.models.ApiResponse
import com.example.helper.domen.models.Archive
import com.example.helper.domen.models.Document
import com.example.helper.domen.models.Fund
import kotlinx.coroutines.flow.Flow

interface ArchivesRepository {
    fun getArchives(): Flow<ApiResponse<List<Archive>>>
    suspend fun getArchiveUpdate():ApiResponse<List<Archive>>
    suspend fun getInventoryByFundId(fundId:String):ApiResponse<List<Document>>
    fun getFundById(fundId: String): Flow<ApiResponse<Fund>>
    fun getArchiveById(archiveId:String):Flow<ApiResponse<Archive>>
}
