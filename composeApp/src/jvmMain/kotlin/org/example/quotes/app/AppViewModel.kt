package org.example.quotes.app

import AppCore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppViewModelState(val searchTerm: String = "", val isAddQuoteModalOpen: Boolean = false, val isFilterQuotesModalOpen: Boolean = false)

class AppViewModel(private val appCore: AppCore) : ViewModel() {
    private val _state = MutableStateFlow(AppViewModelState())
    val state = _state.asStateFlow()

    //later, when I move functions that need to trigger a snackbar notif, emit to above. Put a LaunchedEffect in UI that listens to messages and just calls showSnackbar
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    private val _focusRequest = MutableSharedFlow<Unit>()
    val focusRequest = _focusRequest.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(50)
            _focusRequest.emit(Unit)
        }
    }

    fun updateSearchTerm(newVal: String) {
        _state.update { currState -> currState.copy(searchTerm = newVal) }
    }

    fun showAddQuoteModal() {
        _state.update { currState -> currState.copy(isAddQuoteModalOpen = true) }
    }
    fun hideAddQuoteModal() {
        _state.update { currState -> currState.copy(isAddQuoteModalOpen = false) }
        viewModelScope.launch {
            _focusRequest.emit(Unit)
        }
    }


    fun showFilterQuotesModal() {
        _state.update { currState -> currState.copy(isFilterQuotesModalOpen = true) }
    }
    fun hideFilterQuotesModal() {
        _state.update { currState -> currState.copy(isFilterQuotesModalOpen = false) }
        viewModelScope.launch {
            _focusRequest.emit(Unit)
        }
    }
}