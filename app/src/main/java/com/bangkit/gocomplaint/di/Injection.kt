package com.bangkit.gocomplaint.di

import android.content.Context
import com.bangkit.gocomplaint.data.api.ApiConfig
import com.bangkit.gocomplaint.data.pref.UserPreference
import com.bangkit.gocomplaint.data.pref.dataStore
import com.bangkit.gocomplaint.data.repository.ComplaintRepository
import com.bangkit.gocomplaint.data.repository.NeedHeaderRepository
import com.bangkit.gocomplaint.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection{

    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }

    fun provideComplaintRepository(): ComplaintRepository {
        val apiService = ApiConfig.getApiService()
        return ComplaintRepository.getInstance(apiService)
    }

    fun provideHeaderRepository(context: Context): NeedHeaderRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return NeedHeaderRepository.getInstance(pref, apiService)
    }
}