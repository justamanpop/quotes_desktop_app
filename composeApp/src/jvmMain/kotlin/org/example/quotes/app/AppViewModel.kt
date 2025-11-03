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
import java.util.Locale.getDefault

data class AppViewModelState(
    val isAddQuoteModalOpen: Boolean = false,
    val isFilterQuotesModalOpen: Boolean = false,
    val isManageTagsModalOpen: Boolean = false,

    val isEditQuoteModalOpen: Boolean = false,
    val quoteClickedForEdit: Quote? = null,

    val searchTerm: String = "",

    val quotes: List<Quote> = listOf(),
    val tags: List<Tag> = listOf(),

    val filterTags: Set<Tag> = setOf(),
)

class AppViewModel(private val appCore: AppCore) : ViewModel() {
    private val _state = MutableStateFlow(AppViewModelState(quotes = appCore.getQuotes(), tags = appCore.getTags()))
    val state = _state.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage.asSharedFlow()
    private fun emitSnackbarMessage(message: String) {
        viewModelScope.launch {
            _snackbarMessage.emit(message)
        }
    }
    fun showSnackbarMessage(message: String) {
        emitSnackbarMessage(message)
    }

    private val _focusRequest = MutableSharedFlow<Unit>()
    val focusRequest = _focusRequest.asSharedFlow()
    private fun requestFocus() {
        viewModelScope.launch {
            _focusRequest.emit(Unit)
        }
    }

    init {
        viewModelScope.launch {
            delay(50)
            _focusRequest.emit(Unit)
        }
    }

    fun fetchQuotes() {
        try {
            val quotes = appCore.getQuotes()
            _state.update { currState -> currState.copy(quotes = quotes) }
        } catch (error: Exception) {
            emitSnackbarMessage("Error: Unable to fetch quotes, ${error.message}")
            return
        }
    }
    fun addQuote(quote: Quote) {
        try {
            appCore.addQuote(quote)
        } catch (error: Exception) {
            emitSnackbarMessage("Error: Unable to add quote, ${error.message}")
            return
        }

        emitSnackbarMessage("Success: Quote successfully added!")
        fetchQuotes()
        requestFocus()
    }
    fun updateQuote(quote: Quote) {
        try {
            appCore.updateQuote(quote)
        } catch (error: Exception) {
            emitSnackbarMessage("Error: Unable to update quote, ${error.message}")
            return
        }

        emitSnackbarMessage("Success: Quote successfully updated!")
        fetchQuotes()
        requestFocus()
    }
    fun deleteQuote(quoteId: Int) {
        try {
            appCore.deleteQuote(quoteId)
        } catch (error: Exception) {
            emitSnackbarMessage("Error: Unable to delete quote, ${error.message}")
            return
        }
        emitSnackbarMessage("Success: Quote successfully deleted!")
        fetchQuotes()
    }

    fun fetchTags() {
        try {
            val tags = appCore.getTags()
            _state.update { currState -> currState.copy(tags = tags) }
        } catch (error: Exception) {
            emitSnackbarMessage("Error: Unable to fetch tags, ${error.message}")
            return
        }
    }

    fun addTag(tag: Tag) {
        try {
            appCore.addTag(tag)
        } catch (error: Exception) {
            emitSnackbarMessage("Error: Unable to add tag, ${error.message}")
            return
        }
        emitSnackbarMessage("Success: Tag successfully added!")
        fetchTags()
    }
    fun updateTagName(tagId: Int, newName: String) {
        try {
            appCore.updateTag(tagId, newName)
        } catch (error: Exception) {
            emitSnackbarMessage("Error: Unable to update tag, ${error.message}")
            return
        }
        emitSnackbarMessage("Success: Tag successfully updated!")
        fetchTags()
        syncUpdatedTagForEachQuote(tagId, newName)
    }
    fun deleteTag(tagId: Int) {
        try {
            appCore.deleteTag(tagId)
        } catch (error: Exception) {
            emitSnackbarMessage("Error: Unable to delete tag, ${error.message}")
            return
        }
        emitSnackbarMessage("Success: Tag successfully deleted!")
        fetchTags()
        removeDeletedTagForEachQuote(tagId)
    }

    fun deriveFilteredQuotes(): List<Quote> {
        val searchTerm = state.value.searchTerm.lowercase(getDefault())
        val tags = state.value.filterTags
        val quotes = state.value.quotes

        val filteredQuotes = quotes.filter { q ->
            val matchesSearch = if (searchTerm.isBlank()) {
                true
            } else {
                val lowerCaseContent = q.content.lowercase(getDefault())
                val lowerCaseSource = q.source.lowercase(getDefault())
                lowerCaseContent.contains(searchTerm) || lowerCaseSource.contains(searchTerm)
            }

            val matchesTags = if (tags.isEmpty()) {
                true
            } else {
                q.tags.containsAll(tags)
            }
            matchesSearch && matchesTags
        }

        return filteredQuotes
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
        requestFocus()
    }


    fun showFilterQuotesModal() {
        _state.update { currState -> currState.copy(isFilterQuotesModalOpen = true) }
    }

    fun hideFilterQuotesModal() {
        _state.update { currState -> currState.copy(isFilterQuotesModalOpen = false) }
        requestFocus()
    }

    fun showEditQuoteModal(quote: Quote) {
        _state.update { currState -> currState.copy(quoteClickedForEdit = quote, isEditQuoteModalOpen = true) }
    }

    fun hideEditQuoteModal() {
        _state.update { currState -> currState.copy(isEditQuoteModalOpen = false) }
        requestFocus()
    }

    fun showManageTagsModal() {
        _state.update { currState -> currState.copy(isManageTagsModalOpen = true) }
    }

    fun hideManageTagsModal() {
        _state.update { currState -> currState.copy(isManageTagsModalOpen = false) }
        requestFocus()
    }
}