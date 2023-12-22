package com.bangkit.gocomplaint.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkit.gocomplaint.data.model.AddCommentRequest
import com.bangkit.gocomplaint.data.model.AddComplaintResponse
import com.bangkit.gocomplaint.data.model.ComplaintsItem
import com.bangkit.gocomplaint.data.pref.UserModel
import com.bangkit.gocomplaint.data.repository.ComplaintRepository
import com.bangkit.gocomplaint.data.repository.NeedHeaderRepository
import com.bangkit.gocomplaint.data.repository.UserRepository
import com.bangkit.gocomplaint.ui.common.UiState
import com.bangkit.gocomplaint.ui.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: UserRepository,
    private val complaintRepository: ComplaintRepository,
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> get() = _startDestination

    private val _uiState: MutableStateFlow<UiState<UserModel>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<UserModel>>
        get() = _uiState

    val complaint: Flow<PagingData<ComplaintsItem>> =
        complaintRepository.getComplaints()

    init {
        getSession()
    }

    fun getSession() {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()
            repository.getSession().collect { user ->
                if (user.token != "" && user.expiryTime != 0L) {
                    if (user.expiryTime > currentTime) {
                        _startDestination.value = Screen.Home.route
                    } else {
                        repository.logout()
                        _startDestination.value = Screen.Login.route
                    }
                } else {
                    repository.logout()
                    _startDestination.value = Screen.Login.route
                }
            }
        }
    }

}