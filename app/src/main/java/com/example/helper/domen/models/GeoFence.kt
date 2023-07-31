package com.example.helper.domen.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.helper.utils.Constants.DATABASE_TABLE_NAME
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Suppress("DEPRECATED_ANNOTATION")
@Entity(tableName = DATABASE_TABLE_NAME)
@Parcelize
data class GeoFence(
    @SerializedName("geoId")
    val geoId: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("time")
    val time: String,
    @SerializedName("year")
    val year: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude:Double,
    @SerializedName("radius")
    val radius: Double,
    @SerializedName("createdBy")
    val createdBy:String?=null
): Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var geofenceId: Int = 0
}