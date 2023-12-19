package com.bangkit.gocomplaint.data.repository

import com.bangkit.gocomplaint.data.api.ApiService
import com.bangkit.gocomplaint.data.model.AuthResponse
import com.bangkit.gocomplaint.data.model.LoginRequest
import com.bangkit.gocomplaint.data.model.RegisterRequest
import com.bangkit.gocomplaint.data.pref.UserModel
import com.bangkit.gocomplaint.data.pref.UserPreference
import com.bangkit.gocomplaint.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    suspend fun register(registerRequest: RegisterRequest): UiState<AuthResponse> {
        return try {
            val response = apiService.register(registerRequest)
            if (response.isSuccessful) {
                UiState.Success(response.body() ?: throw Exception("Empty response body"))
            } else {
                UiState.Error("Registration failed")
            }
        } catch (exception: Exception) {
            UiState.Error(exception.message.toString())
        }
    }

    suspend fun login(loginRequest: LoginRequest): UiState<AuthResponse> {
        return try {
            val response = apiService.login(loginRequest)
            if (response.isSuccessful) {
                UiState.Success(response.body() ?: throw Exception("Empty response body"))
            } else {
                UiState.Error("Login failed")
            }
        } catch (exception: Exception) {
            UiState.Error(exception.message.toString())
        }
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