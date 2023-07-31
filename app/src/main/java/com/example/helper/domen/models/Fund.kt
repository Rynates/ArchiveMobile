package com.example.helper.domen.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.helper.utils.Constants
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Entity(tableName = Constants.FUND_DATABASE_TABLE_NAME)
@Parcelize
data class Fund(
    @PrimaryKey
    val id: String,
    val number:Int,
    val name: String,
    val inventoryNum:String?=null,
    var inventoryList:List<String>? = null,
    val digitization:String?=null,
    @Expose(serialize = false, deserialize = false)
    var isSaves:Boolean = false,
    var archiveId:String?=null
): Parcelable

