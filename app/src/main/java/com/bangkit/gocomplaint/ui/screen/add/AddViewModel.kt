package com.bangkit.gocomplaint.ui.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.gocomplaint.data.model.AddComplaintResponse
import com.bangkit.gocomplaint.data.pref.UserModel
import com.bangkit.gocomplaint.data.repository.NeedHeaderRepository
import com.bangkit.gocomplaint.data.repository.UserRepository
import com.bangkit.gocomplaint.ui.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class AddViewModel(private val repository: UserRepository, private val needHeaderRepository: NeedHeaderRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<AddComplaintResponse>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<AddComplaintResponse>>
        get() = _uiState

    private val _uiUserState = MutableStateFlow<UserModel?>(null)
    val uiUserState: StateFlow<UserModel?> get() = _uiUserState

    init {
        getAccessToken()
        getSession()
    }

    fun addComplaint(complaint: String, category: String, location: String, file: File) {
        _uiState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            needHeaderRepository.addComplaint(complaint, category, location, file)
                .collect { data ->
                    _uiState.value = UiState.Success(data)
                }
        }
    }

    fun getAccessToken() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSession().collect { userEntity ->
                _uiUserState.value = userEntity
            }
        }
    }

    fun getSession() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSession()
        }
    }
}