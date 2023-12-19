package com.bangkit.gocomplaint.data.model

import com.google.gson.annotations.SerializedName

data class DetailResponse(

	@field:SerializedName("comments")
	val comments: List<CommentsItem>,

	@field:SerializedName("complaint")
	val complaint: Complaint
)

data class Complaint(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("complaint")
	val complaint: String,

	@field:SerializedName("like")
	val like: Int,

	@field:SerializedName("prediction")
	val prediction: String,

	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String,

	@field:SerializedName("username")
	val username: String
)

data class CommentsItem(

	@field:SerializedName("complaint_id")
	val complaintId: Int,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("like")
	val like: Int,

	@field:SerializedName("comment")
	val comment: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("updatedAt")
	val updatedAt: String,

	@field:SerializedName("username")
	val username: String
)
