package com.example.helper.data.locale

import androidx.room.*
import com.example.helper.domen.models.Document
import com.example.helper.domen.models.Fund
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryDao {

    @Query("SELECT * FROM inventory_table ORDER BY id ASC")
    fun readInventories(): Flow<MutableList<Document>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addInventories(fund: Document)

    @Delete
    suspend fun deleteInventories(fund: Document)
}
