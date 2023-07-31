package com.example.helper.data.locale

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.helper.domen.models.Document
import com.example.helper.domen.models.RelativeResponce
import com.example.helper.utils.Converters

@Database(
    entities = [Document::class],
    version = 20,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class InventoryDatabase: RoomDatabase() {
    abstract fun inventoryDao():InventoryDao

}