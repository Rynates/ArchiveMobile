package com.example.helper.domen.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse <T>(
    @SerializedName("success")
    val success:Boolean,
    @SerializedName("info")
    val info: T? = null,
    @SerializedName("message")
    val message:String? = null,
    val error:String? = null
)
