package com.bangkit.gocomplaint.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.gocomplaint.data.model.DetailResponse
import com.bangkit.gocomplaint.data.pref.UserModel
import com.bangkit.gocomplaint.data.repository.ComplaintRepository
import com.bangkit.gocomplaint.data.repository.UserRepository
import com.bangkit.gocomplaint.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: UserRepository, private val complaintRepository: ComplaintRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<DetailResponse>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<DetailResponse>>
        get() = _uiState

    fun getDetailComplaint(id: String) {
        viewModelScope.launch {
            complaintRepository.getDetailComplaint(id)
                .collect { Profile ->
                    _uiState.value = UiState.Success(Profile as DetailResponse)
                }
        }
    }

    fun getSession(): Flow<UserModel> = repository.getSession()

}