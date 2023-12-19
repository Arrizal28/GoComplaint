package com.bangkit.gocomplaint.data.model

import com.google.gson.annotations.SerializedName

data class ComplaintResponse(

    @field:SerializedName("complaints")
    val complaints: List<ComplaintsItem> = emptyList(),
)

data class ComplaintsItem(

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("file")
    val file: Any? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("complaint")
    val complaint: String? = null,

    @field:SerializedName("like")
    val like: Int? = null,

    @field:SerializedName("prediction")
    val prediction: Any? = null,

    @field:SerializedName("location")
    val location: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null,

    @field:SerializedName("username")
    val username: String? = null,
)
