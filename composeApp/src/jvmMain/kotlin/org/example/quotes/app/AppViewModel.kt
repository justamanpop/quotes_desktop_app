package org.example.quotes.app

import AppCore
import Quote
import Tag
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppViewModelState(
    val isAddQuoteModalOpen: Boolean = false,
    val isFilterQuotesModalOpen: Boolean = false,
    val isManageTagsModalOpen: Boolean = false,

    val isEditQuoteModalOpen: Boolean = false,
    val quoteClickedForEdit: Quote? = null,

    val searchTerm: String = "",

    val quotes: List<Quote> = listOf(),

    val filterTags: Set<Tag> = setOf(),
)

class AppViewModel(private val appCore: AppCore) : ViewModel() {
    private val _state = MutableStateFlow(AppViewModelState(quotes = appCore.getQuotes()))
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

    fun fetchQuotes() {
        _state.update { currState -> currState.copy(quotes = appCore.getQuotes()) }
    }

    fun syncUpdatedTagForEachQuote(idOfUpdatedTag: Int, newTagName: String) {
        val updatedQuotes = state.value.quotes.map { quote ->
            quote.copy(tags = quote.tags.map { tag ->
                if (tag.id == idOfUpdatedTag) {
                    tag.copy(name = newTagName)
                } else {
                    tag
                }
            })
        }
        _state.update { currState -> currState.copy(quotes = updatedQuotes) }
    }
    fun removeDeletedTagForEachQuote(idOfDeletedTag: Int) {
        val updatedQuotes = state.value.quotes.map { quote ->
            quote.copy(tags = quote.tags.filterNot { tag ->
                tag.id == idOfDeletedTag
            })
        }
        _state.update { currState -> currState.copy(quotes = updatedQuotes) }
    }

    fun updateSearchTerm(newVal: String) {
        _state.update { currState -> currState.copy(searchTerm = newVal) }
    }
    fun updateFilterTags(filterTags: Set<Tag>) {
        _state.update { currState -> currState.copy(filterTags = filterTags) }
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

    fun showEditQuoteModal(quote: Quote) {
        _state.update { currState -> currState.copy(quoteClickedForEdit = quote, isEditQuoteModalOpen = true) }
    }

    fun hideEditQuoteModal() {
        _state.update { currState -> currState.copy(isEditQuoteModalOpen = false) }
        viewModelScope.launch {
            _focusRequest.emit(Unit)
        }
    }

    fun showManageTagsModal() {
        _state.update { currState -> currState.copy(isManageTagsModalOpen = true) }
    }

    fun hideManageTagsModal() {
        _state.update { currState -> currState.copy(isManageTagsModalOpen = false) }
        viewModelScope.launch {
            _focusRequest.emit(Unit)
        }
    }
}