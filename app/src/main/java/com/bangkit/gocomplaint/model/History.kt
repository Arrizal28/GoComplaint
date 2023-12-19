package com.bangkit.gocomplaint.model

data class Profile (
    val id: Long,
    val image: Int,
    val username: String,
    val date: String,
    val complaint: String,
)