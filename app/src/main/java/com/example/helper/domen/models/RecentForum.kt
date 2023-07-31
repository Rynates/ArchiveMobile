package com.example.helper.domen.models

import kotlinx.serialization.Serializable

@Serializable
data class RecentForum(
    var id:String? = null,
    var topic:String? = null,
    var text:String? = null,
    var avatar:String? = null
)
