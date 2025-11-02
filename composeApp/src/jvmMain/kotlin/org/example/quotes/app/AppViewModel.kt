package org.example.quotes.app

import AppCore
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AppViewModelState(val searchTerm: String = "")

class AppViewModel(private val appCore: AppCore) : ViewModel() {
    private val _state = MutableStateFlow(AppViewModelState())
    val state = _state.asStateFlow()

    fun updateSearchTerm(newVal: String) {
        _state.update { currState -> currState.copy(searchTerm = newVal) }
    }
}