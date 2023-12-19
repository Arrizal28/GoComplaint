package com.bangkit.gocomplaint.data.api

import com.bangkit.gocomplaint.data.model.AddComplaintRequest
import com.bangkit.gocomplaint.data.model.AddComplaintResponse
import com.bangkit.gocomplaint.data.model.AuthResponse
import com.bangkit.gocomplaint.data.model.ComplaintResponse
import com.bangkit.gocomplaint.data.model.DetailResponse
import com.bangkit.gocomplaint.data.model.LoginRequest
import com.bangkit.gocomplaint.data.model.RegisterRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("api/auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<AuthResponse>

//    @GET("api/main/complaints")
//    suspend fun getComplaints(): ComplaintResponse

    @GET("api/main/complaints")
    suspend fun getComplaints(
        @Query("limit") limit: Int = 5,
        @Query("page") page: Int = 1
    ): ComplaintResponse

    @GET("api/main/history/complaints/{id}")
    suspend fun getHistory(
        @Path("id") id: String
    ): ComplaintResponse

    @GET("api/main/complaints/{id}")
    suspend fun getDetailComplaint(
        @Path("id") id: String
    ): DetailResponse

    @GET("api/main/search")
    suspend fun getSearchComplaints(
        @Query("complaint") complaint: String,
    ): ComplaintResponse

    @Multipart
    @POST("api/main/complaints")
    suspend fun addComplaint(
        @Part("complaint") description: RequestBody,
        @Part("category") category: RequestBody,
        @Part("location") location: RequestBody,
        @Part file: MultipartBody.Part,
    ): AddComplaintResponse

    @POST("api/main/comments")
    suspend fun addComment(
        @Body addComplaintRequest: AddComplaintRequest
    ): AddComplaintResponse
}