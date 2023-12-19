package com.bangkit.gocomplaint.data.model

import android.net.Uri

data class AddComplaintRequest(
    val complaint: String,
    val category: String,
    val location: String,
    val file: Uri,
)