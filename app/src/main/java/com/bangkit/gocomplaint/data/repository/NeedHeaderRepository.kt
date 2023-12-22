package com.bangkit.gocomplaint.data.repository

import com.bangkit.gocomplaint.data.api.ApiService
import com.bangkit.gocomplaint.data.model.AddCommentRequest
import com.bangkit.gocomplaint.data.pref.UserPreference
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class NeedHeaderRepository(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    fun getDetailHistory(id: String) = flow {
        try {
            val successResponse = apiService.getHistory(id)
            emit(successResponse)
        } catch (e: HttpException) {

        }
    }

    fun addComplaint(complaint: String, category: String, location: String, file: File) =
        flow {
            val requestComplaint = complaint.toRequestBody("text/plain".toMediaType())
            val requestCategory = category.toRequestBody("text/plain".toMediaType())
            val requestLocation = location.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "file",
                file.name,
                requestImageFile
            )
            try {
                val successResponse = apiService.addComplaint(
                    requestComplaint,
                    requestCategory,
                    requestLocation,
                    multipartBody
                )
                emit(successResponse)
            } catch (e: HttpException) {
            }
        }

    fun addComment(addCommentRequest: AddCommentRequest) = flow {
        try {
            val successResponse = apiService.addComment(addCommentRequest)
            emit(successResponse)
        } catch (e: HttpException) {
        }
    }

    fun deleteComplaint(id: String) = flow {
        try {
            val successResponse = apiService.deleteComplaint(id)
            emit(successResponse)
        } catch (e: HttpException) {
        }
    }

    companion object {
        @Volatile
        private var instance: NeedHeaderRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): NeedHeaderRepository =
            instance ?: synchronized(this) {
                instance ?: NeedHeaderRepository(userPreference, apiService)
            }.also { instance = it }
    }
}