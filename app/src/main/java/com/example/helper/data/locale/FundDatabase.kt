package com.example.helper.data.locale

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.helper.domen.models.Fund
import com.example.helper.utils.Converters

@Database(
    entities = [Fund::class],
    version = 8,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FundDatabase: RoomDatabase() {
    abstract fun fundsDao():FundDao

}
