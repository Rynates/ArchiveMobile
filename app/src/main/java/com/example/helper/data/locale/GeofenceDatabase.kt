package com.example.helper.data.locale

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.helper.domen.models.GeoFence
import com.example.helper.utils.Converters

@Database(
    entities = [GeoFence::class],
    version = 9,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GeofenceDatabase: RoomDatabase() {

    abstract fun geofenceDao(): GeofenceDao

}