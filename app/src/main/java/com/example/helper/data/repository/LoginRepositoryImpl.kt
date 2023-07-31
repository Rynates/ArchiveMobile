package com.example.helper.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.example.helper.data.remote.KtorApi
import com.example.helper.domen.models.*
import com.example.helper.domen.repository.LoginRepository
import retrofit2.HttpException
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val ktorApi: KtorApi,
    private val prefs: SharedPreferences
) : LoginRepository {
    //    override suspend fun verifyTokenOnServer(request: ApiRequest): ApiResponse {
//        return try{
//            ktorApi.verifyTokenOnServer(request = request)
//        }catch (e:Exception){
//            ApiResponse(success = false, error = e.message)
//        }
//    }
    override suspend fun signIn(email: String?, password: String?): AuthResult<Unit> {
        return try {
            val token = ktorApi.signIn(request = AuthRequest(email = email!!, password = password!!))

            Log.d("TokenSignIn", token.token)
            prefs.edit()
                .putString("jwt", token.token)
                .apply()
            AuthResult.Authorized()
        } catch (e: Exception) {
            AuthResult.Unauthorized()
        }

    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null) ?: return AuthResult.Unauthorized()
            Log.d("TokenSign", token)
            AuthResult.Authorized()
        } catch (e: HttpException) {
            AuthResult.Unauthorized()
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun signOut(): AuthResult<Unit> {
        return try {
            ktorApi.signOut()
            AuthResult.Unauthorized()
        } catch (e: HttpException) {
            AuthResult.Unauthorized()
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }
}