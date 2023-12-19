package com.bangkit.gocomplaint.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bangkit.gocomplaint.data.api.ApiService
import com.bangkit.gocomplaint.data.model.ComplaintResponse
import com.bangkit.gocomplaint.data.model.ComplaintsItem
import com.bangkit.gocomplaint.data.paging.ComplaintPagingSource
import com.bangkit.gocomplaint.ui.common.UiState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class ComplaintRepository(
    private val apiService: ApiService
) {
//    fun getComplaints() = flow {
//        emit(UiState.Loading)
//        try {
//            val successResponse = apiService.getComplaints()
//            emit(UiState.Success(successResponse))
//        } catch (e: HttpException) {
//            emit(UiState.Error(e.message.toString()))
//        }
//    }

    fun getComplaints(): Flow<PagingData<ComplaintsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                ComplaintPagingSource(apiService)
            }
        ).flow
    }

//    fun getDetailProfile(id: String) = flow {
//        try {
//            val successResponse = apiService.getProfile(id)
//            emit(successResponse)
//        } catch(e: HttpException) {
//            emit("Failed To Load Data")
//        }
//    }

    fun getSearchComplaint(query: String) = flow {
        try {
            val successResponse = apiService.getSearchComplaints(query)
            emit(successResponse)
        } catch(e: HttpException) {
            emit("Failed To Load Data")
        }
    }

    fun getDetailComplaint(id: String) = flow {
        try {
            val successResponse = apiService.getDetailComplaint(id)
            emit(successResponse)
        } catch(e: HttpException) {
            emit("Failed To Load Data")
        }
    }

    companion object {
        @Volatile
        private var instance: ComplaintRepository? = null
        fun getInstance(
            apiService: ApiService
        ): ComplaintRepository =
            instance ?: synchronized(this) {
                instance ?: ComplaintRepository(apiService)
            }.also { instance = it }
    }
}