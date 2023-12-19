package com.bangkit.gocomplaint.data.model

data class PagingRequest (
    val limit: Int = 10,
    val page: Int = 1,
)