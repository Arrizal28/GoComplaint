package com.bangkit.gocomplaint.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bangkit.gocomplaint.data.api.ApiService
import com.bangkit.gocomplaint.data.model.ComplaintsItem
import com.bangkit.gocomplaint.data.paging.ComplaintPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class ComplaintRepository(
    private val apiService: ApiService
) {
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

    fun getSearchComplaint(query: String) = flow {
        try {
            val successResponse = apiService.getSearchComplaints(query)
            emit(successResponse)
        } catch(e: HttpException) {
            throw e
        }
    }

    fun getDetailComplaint(id: String) = flow {
        try {
            val successResponse = apiService.getDetailComplaint(id)
            emit(successResponse)
        } catch(e: HttpException) {
            throw e
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