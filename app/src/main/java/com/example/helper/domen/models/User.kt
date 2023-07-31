package com.example.helper.domen.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerializedName("name")
    val name: String?=null,
    @SerializedName("email")
    val email: String?=null,
    @SerializedName("password")
    val password:String?=null,
    @SerializedName("location")
    val location:String?=null,
    @SerializedName("info")
    val info:String?=null,
    @SerializedName("salt")
    val salt:String?=null,
    @SerializedName("photo")
    var photo:String?=null,
    @SerializedName("id")
    val id: String?=null,
    @SerializedName("isEmailOpen")
    var isEmailOpen:Boolean = true
)