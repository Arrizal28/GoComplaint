package com.bangkit.gocomplaint.model

data class Complaint (
    val id: Long,
    val image: Int,
    val username: String,
    val date: String,
    val complaint: String,
    val upvote: Int,
    val comment: Int,
)