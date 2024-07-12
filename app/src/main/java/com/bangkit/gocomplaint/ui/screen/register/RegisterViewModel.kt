package com.bangkit.gocomplaint.ui.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.gocomplaint.data.model.AuthResponse
import com.bangkit.gocomplaint.data.model.RegisterRequest
import com.bangkit.gocomplaint.data.pref.UserModel
import com.bangkit.gocomplaint.data.repository.UserRepository
import com.bangkit.gocomplaint.ui.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<AuthResponse>>(UiState.Loading)
    val uiState: StateFlow<UiState<AuthResponse>> get() = _uiState

    private val _uiRegisState = MutableStateFlow<UserModel?>(null)
    val uiRegisState: StateFlow<UserModel?> get() = _uiRegisState

//    fun register(registerRequest: RegisterRequest, context: Context) {
//        _uiState.value = UiState.Loading
//        viewModelScope.launch {
//            val uiState = repository.register(registerRequest, context)
//            val expiryTime = Calendar.getInstance().apply {
//                add(Calendar.DAY_OF_MONTH, 30)
//            }.timeInMillis
//            _uiState.value = when (uiState) {
//                is UiState.Success -> {
//                    repository.saveSession(UserModel(
//                        userId = uiState.data.id,
//                        token = uiState.data.accessToken,
//                        refreshToken = uiState.data.refreshToken,
//                        expiryTime = expiryTime
//                    ))
//                    UiState.Success(uiState.data)
//                }
//                is UiState.Error -> UiState.Error(uiState.errorMessage)
//                UiState.Loading -> UiState.Loading
//            }
//        }
//    }

    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.register(registerRequest).catch {
                _uiState.value = UiState.Error(it.message.toString())
            }
                .collect { response ->
                    _uiState.value = UiState.Success(response)
                }
        }
    }


    fun getAccessToken() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSession().collect { userEntity ->
                _uiRegisState.value = userEntity
            }
        }
    }
}