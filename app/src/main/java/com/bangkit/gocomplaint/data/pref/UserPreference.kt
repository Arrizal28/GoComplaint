package com.bangkit.gocomplaint.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = user.userId
            preferences[TOKEN_KEY] = user.token
            preferences[REFRESH_TOKEN] = user.refreshToken
            preferences[EXPIRY_TIME] = user.expiryTime
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[USER_ID]  ?: 0,
                preferences[TOKEN_KEY] ?: "",
                preferences[REFRESH_TOKEN] ?: "",
                preferences[EXPIRY_TIME] ?: 0L
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val USER_ID = intPreferencesKey("userid")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val EXPIRY_TIME = longPreferencesKey("expiry_time")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}