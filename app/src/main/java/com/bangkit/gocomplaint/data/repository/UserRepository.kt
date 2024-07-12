package com.bangkit.gocomplaint.data.repository

import com.bangkit.gocomplaint.data.api.ApiService
import com.bangkit.gocomplaint.data.model.AuthResponse
import com.bangkit.gocomplaint.data.model.LoginRequest
import com.bangkit.gocomplaint.data.model.RegisterRequest
import com.bangkit.gocomplaint.data.pref.UserModel
import com.bangkit.gocomplaint.data.pref.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.Calendar

class UserRepository(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
//    suspend fun register(
//        registerRequest: RegisterRequest,
//        context: Context
//    ): UiState<AuthResponse> {
//        val applicationContext = context.applicationContext
//        return try {
//            UiState.Loading
//            val response = apiService.register(registerRequest)
//            if (response.isSuccessful) {
//                UiState.Success(response.body() ?: throw Exception("Empty response body"))
//            } else {
//                val errorResponse = response.errorBody()?.string()
//                val jsonObject = JSONObject(errorResponse ?: "{}")
//                val error = jsonObject.optString("error")
//                val errorMessage = error ?: applicationContext.getString(R.string.unknown_error)
//                UiState.Error(errorMessage)
//            }
//        } catch (exception: Exception) {
//            UiState.Error(applicationContext.getString(R.string.loading_failed))
//        }
//    }

//    suspend fun login(loginRequest: LoginRequest, context: Context): UiState<AuthResponse> {
//        val applicationContext = context.applicationContext
//        return try {
//            UiState.Loading
//            val response = apiService.login(loginRequest)
//            if (response.isSuccessful) {
//                UiState.Success(response.body() ?: throw Exception("Empty response body"))
//            } else {
//                val errorResponse = response.errorBody()?.string()
//                val jsonObject = JSONObject(errorResponse ?: "{}")
//                val error = jsonObject.optString("error")
//                val errorMessage = error ?: applicationContext.getString(R.string.unknown_error)
//                UiState.Error(errorMessage)
//            }
//        } catch (exception: Exception) {
//            UiState.Error(applicationContext.getString(R.string.loading_failed))
//        }
//    }

    suspend fun register(registerRequest: RegisterRequest): Flow<AuthResponse> = flow {
        val response = apiService.register(registerRequest)
        if (response.isSuccessful) {
            val data = response.body() ?: throw Exception("Empty response body")
            saveSessionLogin(data)
            emit(data)
        } else {
            throw Exception("Couldn't connect. Try again later.")
        }
    }.flowOn(Dispatchers.IO)

    suspend fun login(loginRequest: LoginRequest): Flow<AuthResponse> = flow {
        val response = apiService.login(loginRequest)
        if (response.isSuccessful) {
            val data = response.body() ?: throw Exception("Empty response body")
            saveSessionLogin(data)
            emit(data)
        } else {
            throw Exception("Couldn't connect. Try again later.")
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun saveSessionLogin(data: AuthResponse) {
        val expiryTime = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 30)
        }.timeInMillis
        saveSession(
            UserModel(
                userId = data.id,
                token = data.accessToken,
                refreshToken = data.refreshToken,
                expiryTime = expiryTime
            )
        )
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}