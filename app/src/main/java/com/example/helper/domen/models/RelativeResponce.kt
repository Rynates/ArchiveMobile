package com.example.helper.domen.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class RelativeResponce(
    val id:String?=null,
    var name:String?=null,
    var surname:String?=null,
    var secondName:String?=null,
    var gender:String?=null,
    //@Serializable(DateSerializer::class)
    var birthDate:String?=null,
    var birthPlace:String?=null,
    var placeOfLiving:String?=null,
    var photo:String?=null,
    var fatherId:String?=null,
    var motherId:String?=null,
    var link:String?=null,
    var livingStatus:String?=null,
    var userId:String,
    var relativeDoc:String?=null
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var relativeId: Int = 1
}