package com.example.helper.data.locale

import androidx.room.*
import com.example.helper.domen.models.Fund
import kotlinx.coroutines.flow.Flow

@Dao
interface FundDao {

    @Query("SELECT * FROM funds_table ORDER BY id ASC")
    fun readFunds(): Flow<MutableList<Fund>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFund(fund:Fund)

    @Delete
    suspend fun deleteFund(fund:Fund)
}

