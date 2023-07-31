package com.example.helper.domen.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.helper.utils.Constants
import kotlinx.parcelize.Parcelize


@Entity(tableName = Constants.INVENTORY_DATABASE_TABLE_NAME)
@Parcelize
data class Document(
    @PrimaryKey
    val id: String,
    val number:Int?=null,
    val name: String?=null,
    var isSaved:Boolean = false,
    var userId:String?=null,
    val years:String?=null,
    val fundId:String,
    val fundNum:Int,
    val inventoryNum:String,
    val archiveId:String?=null,
    val relativeList:List<RelativeResponce> = emptyList()
): Parcelable{
}
