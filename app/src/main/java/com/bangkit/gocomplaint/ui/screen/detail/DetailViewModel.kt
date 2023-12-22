package com.bangkit.gocomplaint.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.gocomplaint.data.model.AddCommentRequest
import com.bangkit.gocomplaint.data.model.AddComplaintResponse
import com.bangkit.gocomplaint.data.model.DetailResponse
import com.bangkit.gocomplaint.data.pref.UserModel
import com.bangkit.gocomplaint.data.repository.ComplaintRepository
import com.bangkit.gocomplaint.data.repository.NeedHeaderRepository
import com.bangkit.gocomplaint.data.repository.UserRepository
import com.bangkit.gocomplaint.ui.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: UserRepository,
    private val complaintRepository: ComplaintRepository,
    private val needHeaderRepository: NeedHeaderRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<DetailResponse>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<DetailResponse>>
        get() = _uiState

    private val _uiCommentState: MutableStateFlow<UiState<AddComplaintResponse>> =
        MutableStateFlow(UiState.Loading)
    val uiCommentState: StateFlow<UiState<AddComplaintResponse>>
        get() = _uiCommentState

    fun getDetailComplaint(id: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            complaintRepository.getDetailComplaint(id)
                .collect { profile ->
                    _uiState.value = UiState.Success(profile as DetailResponse)
                }
        }
    }

    fun addComment(addCommentRequest: AddCommentRequest) {
        _uiCommentState.value = UiState.Loading
        viewModelScope.launch {
            needHeaderRepository.addComment(addCommentRequest)
                .collect { data ->
                    _uiCommentState.value = UiState.Success(data as AddComplaintResponse)
                }
        }
    }


    fun getSession(): Flow<UserModel> = repository.getSession()

}