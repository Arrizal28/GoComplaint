package com.bangkit.gocomplaint.data.model

data class RegisterRequest (
    val username: String,
    val email: String,
    val password: String,
    val confPassword: String
)