package com.example.helper.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.example.helper.data.remote.KtorApi
import com.example.helper.domen.models.ApiRequest
import com.example.helper.domen.models.ApiResponse
import com.example.helper.domen.models.User
import com.example.helper.domen.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


//
//class UserRepositoryImpl @Inject constructor() : UserRepository {
////    private val db = FirebaseFirestore.getInstance()
////    private var auth = Firebase.auth
////    private val userCollection = db.collection("users")
//
//
//    override fun signUp(email: String, password: String): Flow<Result<Boolean>> =
//        flow {
//            val user = auth
//                .createUserWithEmailAndPassword(email, password)
//                .await()
//                .user
//
//            val newUser = User(uid = user!!.uid)
//            addUser(newUser)
//            emit(Result.Success(true))
//        }
//
//    override fun getUserByuId(uId: String): Flow<Result<DocumentSnapshot>> = flow {
//        val result = userCollection.document(uId).get()
//        emit(Result.Success(result.result))
//    }.flowOn(Dispatchers.IO)
//
//    suspend fun addUser(user: User) {
//        db.collection(userCollection.path).document(user.uid).set(user).await()
//    }
//}

class UserRepositoryImpl @Inject constructor(
   private val ktorApi: KtorApi,
   private val prefs: SharedPreferences
) : UserRepository{

    override suspend fun updateUserInfo(user: User): ApiResponse<User> {
       return try{
           val token = prefs.getString("jwt",null) ?:return ApiResponse(success = false)
           val updatedUser = ktorApi.updateUser("Bearer $token", request = ApiRequest(field = user))
           updatedUser.info?.name?.let { Log.d("updateRepo", it) }
           ApiResponse(success = true, info = updatedUser.info)
       }catch (e:Exception){
           ApiResponse(success = false)
       }
    }

    override suspend fun getUserInfo(): ApiResponse<User> {
        return try{
            val token = prefs.getString("jwt",null) ?: return ApiResponse(success = false)
            val user =  ktorApi.getCurrentUser("Bearer $token")
            Log.d("SharedPref",user.info!!.id.toString())
            ApiResponse(success = true,info = user.info)
        }catch (e:Exception){
            ApiResponse(success = false)
        }
    }

    override suspend fun getOtherUserByName(userName: String): ApiResponse<User> {
        return try{
            val user = ktorApi.getUserByName(request = ApiRequest(field = userName))
            ApiResponse(success = true,info = user.info)
        }catch (e:Exception){
            ApiResponse(success = false)
        }
    }

    override suspend fun getUserById(userId: String): ApiResponse<User> {
        val userById = ktorApi.getUserById(request = ApiRequest(field = userId))
        return userById

    }

    override fun getAllUsers(): Flow<ApiResponse<List<User>>> = flow<ApiResponse<List<User>> > {
        try{
            val userList =  ktorApi.getAllUsers()
            emit(ApiResponse(success = true,info = userList.info))
        }catch (e:Exception){
            emit(ApiResponse(success = false))
        }
    }

}
