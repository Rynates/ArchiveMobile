package com.example.helper.domen.models

@kotlinx.serialization.Serializable
data class ApiRequest <T>(val field:T)