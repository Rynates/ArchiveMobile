package com.example.helper.data.locale

import androidx.room.*
import com.example.helper.domen.models.GeoFence
import kotlinx.coroutines.flow.Flow

@Dao
interface GeofenceDao {

    @Query("SELECT * FROM geofence_table ORDER BY geoId ASC")
    fun readGeofences(): Flow<MutableList<GeoFence>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGeofence(geofenceEntity: GeoFence)

    @Delete
    suspend fun removeGeofence(geofenceEntity: GeoFence)

}