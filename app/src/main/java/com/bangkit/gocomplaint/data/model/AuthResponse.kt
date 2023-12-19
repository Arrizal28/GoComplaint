package com.bangkit.gocomplaint.data.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("accessToken")
	val accessToken: String,

	@field:SerializedName("refreshToken")
	val refreshToken: String
)
