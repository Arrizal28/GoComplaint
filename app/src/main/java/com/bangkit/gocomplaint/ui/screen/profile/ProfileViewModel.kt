package com.bangkit.gocomplaint.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.gocomplaint.data.model.ComplaintResponse
import com.bangkit.gocomplaint.data.pref.UserModel
import com.bangkit.gocomplaint.data.repository.NeedHeaderRepository
import com.bangkit.gocomplaint.data.repository.UserRepository
import com.bangkit.gocomplaint.ui.common.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository, private val needHeaderRepository: NeedHeaderRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<ComplaintResponse>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<ComplaintResponse>>
        get() = _uiState

    fun getProfile(id: String) {
        viewModelScope.launch {
            needHeaderRepository.getDetailHistory(id)
                .collect { history ->
                    _uiState.value = UiState.Success(history as ComplaintResponse)
                }
        }
    }

    fun getSession(): Flow<UserModel> = repository.getSession()

    fun logOut() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}