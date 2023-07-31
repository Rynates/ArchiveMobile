package com.example.helper.domen.repository

import com.example.helper.domen.models.ApiResponse
import com.example.helper.domen.models.GeoFence
import com.example.helper.domen.models.User
import kotlinx.coroutines.flow.Flow

interface MapsRepository {
    suspend fun addGeoOnTheMap(place:GeoFence):ApiResponse<GeoFence>
    fun getAllGeofences(): Flow<ApiResponse<List<GeoFence>>>
    suspend fun getGeofenceByUserId(userId:String):ApiResponse<List<GeoFence>>
    suspend fun getUserById(userId:String):ApiResponse<User>//other userId to send intent
    suspend fun deleteGeofence(geofenceId:Int)
}