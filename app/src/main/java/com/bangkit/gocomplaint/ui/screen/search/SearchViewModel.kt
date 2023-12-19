package com.bangkit.gocomplaint.ui.screen.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.gocomplaint.data.model.ComplaintResponse
import com.bangkit.gocomplaint.data.pref.UserModel
import com.bangkit.gocomplaint.data.repository.ComplaintRepository
import com.bangkit.gocomplaint.data.repository.UserRepository
import com.bangkit.gocomplaint.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchViewModel (private val repository: UserRepository, private val complaintRepository: ComplaintRepository) : ViewModel() {
    private val _uiSearchState: MutableStateFlow<UiState<ComplaintResponse>> = MutableStateFlow(UiState.Loading)
    val uiSearchState: StateFlow<UiState<ComplaintResponse>>
        get() = _uiSearchState

    fun getSearchComplaint(newQuery: String) {
        viewModelScope.launch {
            complaintRepository.getSearchComplaint(newQuery)
                .catch {
                    _uiSearchState.value = UiState.Error(it.message.toString())
                }
                .collect { complaint ->
                    _uiSearchState.value = UiState.Success(complaint as ComplaintResponse)
                }
        }
    }

    fun emptySearchResponse() {
        viewModelScope.launch {
            try {
                val emptyResponse = ComplaintResponse()
                _uiSearchState.value = UiState.Success(emptyResponse)
            } catch (e: Exception) {
                _uiSearchState.value = UiState.Error(e.message.toString())
            }
        }
    }
}