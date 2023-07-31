package com.example.helper.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.example.helper.data.remote.KtorApi
import com.example.helper.domen.models.*
import com.example.helper.domen.repository.MapsRepository
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MapsRepositoryImpl @Inject constructor(
    private val prefs: SharedPreferences,
    private val ktorApi: KtorApi
) : MapsRepository {

    private val TAG = this::class.simpleName

    override suspend fun addGeoOnTheMap(place: GeoFence): ApiResponse<GeoFence> {
        return try {
            val token = prefs.getString("jwt", null) ?: return ApiResponse(success = false)
            ktorApi.addGeofence("Bearer $token", request = ApiRequest(field = place))
            ApiResponse(success = true)
        } catch (e: Exception) {
            ApiResponse(success = false, error = e.message)
        }
    }

    override fun getAllGeofences(): Flow<ApiResponse<List<GeoFence>>> = flow {
        val token = prefs.getString("jwt", null) ?: emit(ApiResponse(success = false))
        val listOfGeofences = ktorApi.getAllGeofences("Bearer $token")
        emit(ApiResponse(success = true, info = listOfGeofences.info))

    }

    override suspend fun getGeofenceByUserId(userId:String): ApiResponse<List<GeoFence>> {
        return try {
            val token = prefs.getString("jwt", null) ?: return ApiResponse(success = false)
            val geofence = ktorApi.getGeofenceByUserId("Bearer $token",ApiRequest(field = userId))
            ApiResponse(success = true, info = geofence.info)
        } catch (e: Exception) {
            ApiResponse(success = false, error = e.message)
        }

    }

    override suspend fun getUserById(userId: String): ApiResponse<User> {
        val userById = ktorApi.getUserById(request = ApiRequest(field = userId))
        return userById

    }

    override suspend fun deleteGeofence(geofenceId: Int) {
        val token = prefs.getString("jwt", null)
        ktorApi.deleteGeofence("Bearer $token", request = ApiRequest(field = geofenceId))

    }
//    private val db = FirebaseFirestore.getInstance().collection("places")
//    private val auth = FirebaseAuth.getInstance().currentUser?.uid
//
//    override fun addPlaceOnTheMap(place: Place): Flow<Result<DocumentReference>> = flow {
//
//
//        val postRef = db.add(place).await()
//        emit(Result.Success(postRef))
//
//    }.flowOn(Dispatchers.IO)
//
//    override fun getPlaceOnTheMap(): Flow<Result<List<Place>>> = flow{
//
//        val snapshot = db.get().await()
//        val places = snapshot.toObjects(Place::class.java)
//        emit(Result.Success(places))
//
//    }.flowOn(Dispatchers.IO)

}