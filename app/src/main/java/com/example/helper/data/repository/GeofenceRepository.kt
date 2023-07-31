package com.example.helper.data.repository

import com.example.helper.data.locale.GeofenceDao
import com.example.helper.domen.models.ApiResponse
import com.example.helper.domen.models.GeoFence
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GeofenceRepository @Inject constructor(private val geofenceDao: GeofenceDao) {

    val readGeofences: Flow<MutableList<GeoFence>> = geofenceDao.readGeofences()

    suspend fun addGeofence(geofenceEntity: GeoFence) {
        geofenceDao.addGeofence(geofenceEntity)
    }

    suspend fun removeGeofence(geofenceEntity: GeoFence) {
        geofenceDao.removeGeofence(geofenceEntity)
    }

}