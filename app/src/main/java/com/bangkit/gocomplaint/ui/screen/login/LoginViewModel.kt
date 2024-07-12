package com.bangkit.gocomplaint.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.gocomplaint.data.model.AuthResponse
import com.bangkit.gocomplaint.data.model.LoginRequest
import com.bangkit.gocomplaint.data.pref.UserModel
import com.bangkit.gocomplaint.data.repository.UserRepository
import com.bangkit.gocomplaint.ui.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<AuthResponse>>(UiState.Loading)
    val uiState: StateFlow<UiState<AuthResponse>> get() = _uiState

    private val _uiLoginState = MutableStateFlow<UserModel?>(null)
    val uiLoginState: StateFlow<UserModel?> get() = _uiLoginState

    private val _uiSnackbarState = MutableStateFlow<Boolean>(false)
    val uiSnackbarState: StateFlow<Boolean?> get() = _uiSnackbarState

//    fun login(loginRequest: LoginRequest, context: Context) {
//            viewModelScope.launch {
//            _uiState.value = UiState.Loading
//            val uiState = repository.login(loginRequest, context)
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

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UiState.Loading
            repository.login(loginRequest).catch {
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
                _uiLoginState.value = userEntity
            }
        }
    }
}