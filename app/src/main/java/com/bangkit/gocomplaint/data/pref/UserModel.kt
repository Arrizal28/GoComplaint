package com.bangkit.gocomplaint.data.pref

data class UserModel(
    val userId: Int,
    val token: String,
    val refreshToken: String,
    val expiryTime: Long
)