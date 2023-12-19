package com.bangkit.gocomplaint.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bangkit.gocomplaint.data.api.ApiService
import com.bangkit.gocomplaint.data.model.ComplaintsItem

class ComplaintPagingSource(private val apiService: ApiService): PagingSource<Int, ComplaintsItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ComplaintsItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getComplaints(params.loadSize, position)
            LoadResult.Page(
                data = responseData.complaints,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.complaints.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ComplaintsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}