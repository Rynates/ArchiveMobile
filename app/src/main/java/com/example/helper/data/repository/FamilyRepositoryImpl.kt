package com.example.helper.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.example.helper.data.remote.KtorApi
import com.example.helper.domen.models.*
import com.example.helper.domen.repository.FamilyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class FamilyRepositoryImpl @Inject constructor(
    private val ktorApi: KtorApi,
    private val prefs: SharedPreferences
) : FamilyRepository {
    override suspend fun addRelative(relative: Relative): ApiResponse<List<RelativeResponce>> {
        return try {
            val token = prefs.getString("jwt", null) ?: return ApiResponse(success = false)
            val listOfRelative =
                ktorApi.addRelative("Bearer $token", request = ApiRequest(field = relative))
            ApiResponse(success = true, info = listOfRelative.info)
        } catch (e: Exception) {
            ApiResponse(success = false, error = e.message)
        }
    }

    override suspend fun getRelatives(): ApiResponse<List<RelativeResponce>> {
        return try {
            val token = prefs.getString("jwt", null) ?: return ApiResponse(success = false)
            Log.d("FamilyRep", token)
            val listOfRelatives = ktorApi.getRelatives("Bearer $token")
            ApiResponse(success = true, info = listOfRelatives.info)
        } catch (e: Exception) {
            ApiResponse(success = false, error = e.message)
        }
    }

    override suspend fun getRelativeById(relativeId: String): ApiResponse<RelativeResponce>? {
        return try {
            val token = prefs.getString("jwt", null) ?: return ApiResponse(success = false)
            Log.d("FamilyRep", token)
            val relative = ktorApi.getRelativeById("Bearer $token",ApiRequest(field = relativeId))
            ApiResponse(success = true, info = relative.info)
        } catch (e: Exception) {
            ApiResponse(success = false, error = e.message)
        }
    }


    override suspend fun updateRelative(relative: Relative): ApiResponse<RelativeResponce>? {
        return try {
            val token = prefs.getString("jwt", null) ?: return ApiResponse(success = false)
            Log.d("FamilyRep", token)
            val relative = ktorApi.updateRelative("Bearer $token",ApiRequest(field = relative))
            ApiResponse(success = true, info = relative.info)
        } catch (e: Exception) {
            ApiResponse(success = false, error = e.message)
        }    }

    override suspend fun deleteRelative(relativeId: String) {
        val token = prefs.getString("jwt", null)
        ktorApi.deleteRelative("Bearer $token", request = ApiRequest(field = relativeId))
    }

    override suspend fun getAllRelatives(): ApiResponse<List<RelativeResponce>> {
        return try {
            val token = prefs.getString("jwt", null) ?: return ApiResponse(success = false)
            Log.d("FamilyRep", token)
            val relative = ktorApi.getAllRelatives("Bearer $token")
            ApiResponse(success = true, info = relative.info)
        } catch (e: Exception) {
            ApiResponse(success = false, error = e.message)
        }
    }
//
//    override fun getDocumentById(docId: String): Flow<ApiResponse<Document>> = flow {
//        val token = prefs.getString("jwt", null)
//        val doc = ktorApi.getDocById("Bearer $token", request = ApiRequest(field = docId))
//        emit(ApiResponse(success = true, info = doc.info))
//    }

}