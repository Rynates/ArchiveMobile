package com.example.helper.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.example.helper.data.remote.KtorApi
import com.example.helper.di.IoDispatcher
import com.example.helper.domen.models.*
import com.example.helper.domen.repository.ArchivesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class ArchivesRepositoryImpl @Inject constructor(
    private val ktorApi: KtorApi,
    private val prefs: SharedPreferences,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ArchivesRepository {
    private val TAG = this::class.simpleName

    override fun getArchives(): Flow<ApiResponse<List<Archive>>> = flow {

        val token = prefs.getString("jwt", null) ?: emit(ApiResponse(success = false))
        val listOfArchives = ktorApi.getArchives("Bearer $token")
        emit(listOfArchives)

    }.flowOn(coroutineDispatcher)

    override suspend fun getArchiveUpdate(): ApiResponse<List<Archive>> {
        return try {
            val token = prefs.getString("jwt", null) ?: return ApiResponse(success = false)
            val listOfArchives = ktorApi.getArchives("Bearer $token")
            return ApiResponse(success = true, info = listOfArchives.info)
        } catch (e: Exception) {
            ApiResponse(success = false, error = e.message)
        }

    }

    override suspend fun getInventoryByFundId(fundId: String): ApiResponse<List<Document>> {
        return try {
            val token = prefs.getString("jwt", null) ?: return ApiResponse(success = false)
            val listOfInventories = ktorApi.getInventories(
                "Bearer $token",
                ApiRequest(field = fundId)
            )
            return ApiResponse(success = true, info = listOfInventories.info)
        } catch (e: Exception) {
            ApiResponse(success = false, error = e.message)
        }
    }

    override fun getFundById(fundId: String): Flow<ApiResponse<Fund>> = flow{
            val token = prefs.getString("jwt", null)
            val listOfInventories = ktorApi.getFundById(
                "Bearer $token",
                ApiRequest(field = fundId)
            )
           emit(ApiResponse(success = true, info = listOfInventories.info))
    }

    override fun getArchiveById(archiveId: String): Flow<ApiResponse<Archive>> = flow{
        val token = prefs.getString("jwt", null)
        val listOfInventories = ktorApi.getArchiveById(
            "Bearer $token",
            ApiRequest(field = archiveId)
        )
        emit(ApiResponse(success = true, info = listOfInventories.info))
    }
}

fun String.containsAnyOfIgnoreCase(keywords: List<String>): Boolean {
    for (keyword in keywords) {
        if (this.contains(keyword, true)) return true
    }
    return false

}


