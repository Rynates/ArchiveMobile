package com.example.helper.data.remote

import com.example.helper.domen.models.*
import retrofit2.http.*
import java.util.concurrent.Flow

interface KtorApi {

    @POST("/sign_up")
    suspend fun signUp(
        @Body request: AuthRequest
    ): ApiResponse<User> //work

    @POST("/sign_in")
    suspend fun signIn(
        @Body request: AuthRequest
    ): AuthResponse //work

    @GET("/get_allUsers")
    suspend fun getAllUsers(
    ):ApiResponse<List<User>>

    @POST("/add_relative")
    suspend fun addRelative(
        @Header("Authorization") token: String,
        @Body request: ApiRequest<Relative>
    ): ApiResponse<List<RelativeResponce>>

    @GET("/get_relative")
    suspend fun getRelatives(
        @Header("Authorization") token: String,
    ): ApiResponse<List<RelativeResponce>>

    @POST("/get_relativeById")
    suspend fun getRelativeById(
        @Header("Authorization") token: String,
        @Body request: ApiRequest<String>
    ): ApiResponse<RelativeResponce>

    @GET("/get_user")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String,
    ): ApiResponse<User>

    @PUT("/update_user")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Body request: ApiRequest<User>
    ): ApiResponse<User>

    @GET("/get_archives")
    suspend fun getArchives(
        @Header("Authorization") token: String,
    ): ApiResponse<List<Archive>>

    @POST("/get_archiveById")
    suspend fun getArchiveById(
        @Header("Authorization") token: String,
        @Body request: ApiRequest<String>
    ):ApiResponse<Archive>

    @POST("/get_inventories")
    suspend fun getInventories(
        @Header("Authorization") token: String,
        @Body request: ApiRequest<String>
    ): ApiResponse<List<Document>>

    @POST("/get_documentById")
    suspend fun getDocById(
        @Header("Authorization") token: String,
        @Body request: ApiRequest<String>
    ):ApiResponse<Document>

    @POST("/get_fundById")
    suspend fun getFundById(
        @Header("Authorization") token: String,
        @Body request: ApiRequest<String>
    ): ApiResponse<Fund>


    @POST("/add_geofence")
    suspend fun addGeofence(
        @Header("Authorization") token: String,
        @Body request: ApiRequest<GeoFence>
    ): ApiResponse<GeoFence>

    @POST("/get_geofence")
    suspend fun getGeofenceByUserId(
        @Header("Authorization") token: String,
        @Body request: ApiRequest<String>
    ): ApiResponse<List<GeoFence>>

    @GET("/get_allGeoFences")
    suspend fun getAllGeofences(
        @Header("Authorization") token: String
    ): ApiResponse<List<GeoFence>>

    @POST("/get_otherUserById")
    suspend fun getUserById(
        @Body request: ApiRequest<String>
    ): ApiResponse<User>

    @POST("/get_otherUserByName")
    suspend fun getUserByName(
        @Body request: ApiRequest<String>
    ): ApiResponse<User>

    @GET("/sign_out")
    suspend fun signOut()

    @POST("/delete_geofence")
    suspend fun deleteGeofence(
        @Header("Authorization") token: String,
        @Body request: ApiRequest<Int>
    )

    @PUT("/update_relative")
    suspend fun updateRelative(
        @Header("Authorization") token: String,
        @Body request: ApiRequest<Relative>
    ): ApiResponse<RelativeResponce>

    @POST("/delete_relative")
    suspend fun deleteRelative(
        @Header("Authorization") token: String,
        @Body request: ApiRequest<String>
    )

    @GET("/get_relatives")
    suspend fun getAllRelatives(
        @Header("Authorization") token: String
    ):ApiResponse<List<RelativeResponce>>
}