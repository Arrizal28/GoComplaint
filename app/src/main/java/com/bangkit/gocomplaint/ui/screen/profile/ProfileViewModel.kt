package com.bangkit.gocomplaint.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.gocomplaint.data.model.AddComplaintResponse
import com.bangkit.gocomplaint.data.model.ComplaintResponse
import com.bangkit.gocomplaint.data.repository.NeedHeaderRepository
import com.bangkit.gocomplaint.data.repository.UserRepository
import com.bangkit.gocomplaint.ui.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: UserRepository,
    private val needHeaderRepository: NeedHeaderRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<ComplaintResponse>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<ComplaintResponse>>
        get() = _uiState

    private val _uiDeleteState: MutableStateFlow<UiState<AddComplaintResponse>> =
        MutableStateFlow(UiState.Loading)
    val uiDeleteState: StateFlow<UiState<AddComplaintResponse>>
        get() = _uiDeleteState

    private val _stateId: MutableStateFlow<String> =
        MutableStateFlow("")
    val stateId: StateFlow<String>
        get() = _stateId


    init {
        getSession()
        getSessionId()
    }

    fun getProfile(id: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            needHeaderRepository.getDetailHistory(id).catch {
                _uiState.value = UiState.Error(it.toString())
            }
                .collect { history ->
                    _uiState.value = UiState.Success(history)
                }
        }
    }

    fun deleteComplaint(id: String) {
        _uiDeleteState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            needHeaderRepository.deleteComplaint(id)
                .collect { history ->
                    _uiDeleteState.value = UiState.Success(history)
                }
        }
    }

    private fun updateStateId(newStateId: String) {
        _stateId.value = newStateId
    }

    fun getSessionId() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSession().collect {
                if(it.userId != 0) {
                    updateStateId(it.userId.toString())
                }
            }
        }
    }

    private fun getSession() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSession()
        }
    }


    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.logout()
        }
    }
}