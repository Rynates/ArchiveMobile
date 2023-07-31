package com.example.helper.domen.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Archive(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("city")
    var city: String? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("phone")
    var phone: String? = null,
    @SerializedName("address")
    var address: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("fundList")
    var fundList: List<Fund>,
    @Expose(serialize = false, deserialize = false)
    var isExpandable: Boolean = true
) : Parcelable