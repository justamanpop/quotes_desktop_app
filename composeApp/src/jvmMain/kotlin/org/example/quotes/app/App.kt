package org.example.quotes.app

import Quote
import AppCore
import QuoteTable
import SearchBar
import Tag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import constructSnackbarDataObject
import getSnackbarColor
import kotlinx.coroutines.launch
import moveFocusOnTab
import org.example.quotes.LocalAppGraph
import org.example.quotes.addQuoteModal.AddQuoteModal
import org.example.quotes.editQuoteModal.EditQuoteModal
import org.example.quotes.filterQuotesModal.FilterQuotesModal
import org.example.quotes.manageTagsModal.ManageTagsModal
import stripSnackbarMessage
import java.util.Locale.getDefault
import kotlin.collections.filter

@Composable
fun App(appCore: AppCore) {
    val viewModel: AppViewModel = AppViewModel(LocalAppGraph.current.appCore)

    MaterialTheme {
        val snackbarState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        fun showSnackbar(message: String) {
            scope.launch {
                snackbarState.showSnackbar(message = message)
            }
        }

        Scaffold(
            snackbarHost = {
                Box(modifier = Modifier.fillMaxSize()) {
                    SnackbarHost(hostState = snackbarState, snackbar = { snackbarData ->
                        val containerColor = getSnackbarColor(snackbarData.visuals.message)
                        val updatedSnackbarData =
                            constructSnackbarDataObject(stripSnackbarMessage(snackbarData.visuals.message))

                        Snackbar(updatedSnackbarData, containerColor = containerColor)
                    }, modifier = Modifier.align(Alignment.BottomCenter))
                }
            }
        ) {
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            val currentSearchTerm = remember { mutableStateOf("") }

            val openAddQuoteModal = remember { mutableStateOf(false) }
            fun hideAddQuoteModal() {
                openAddQuoteModal.value = false
                focusRequester.requestFocus()
            }

            val openFilterQuotesModal = remember { mutableStateOf(false) }
            fun hideFilterQuotesModal() {
                openFilterQuotesModal.value = false
                focusRequester.requestFocus()
            }

            val editQuote = remember { mutableStateOf<Quote?>(null) }

            val openEditQuotesModal = remember { mutableStateOf(false) }
            fun hideEditQuotesModal() {
                openEditQuotesModal.value = false
                focusRequester.requestFocus()
            }

            val openManageTagsModal = remember { mutableStateOf(false) }
            fun hideManageTagsModal() {
                openManageTagsModal.value = false
                focusRequester.requestFocus()
            }

            val quotes = remember { mutableStateOf(appCore.getQuotes()) }
            val tagFilters = remember { mutableStateOf(setOf<Tag>()) }

            val filteredQuotes by remember(quotes, currentSearchTerm, tagFilters) {
                derivedStateOf {
                    val searchTerm = currentSearchTerm.value.lowercase(getDefault())
                    val tags = tagFilters.value

                    quotes.value.filter { q ->
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
                }
            }

            fun updateSearchTerm(newVal: String) {
                currentSearchTerm.value = newVal
            }

            fun filterQuotesByTags(tags: Set<Tag>) {
                tagFilters.value = tags
            }

            fun addQuoteInModal(quote: Quote) {
                try {
                    appCore.addQuote(quote)
                } catch (error: Exception) {
                    showSnackbar("Error: Unable to add quote, ${error.message}")
                    return
                }
                showSnackbar("Success: Quote successfully added!")
                quotes.value = appCore.getQuotes()
                focusRequester.requestFocus()
            }

            fun updateQuoteInModal(quote: Quote) {
                try {
                    appCore.updateQuote(quote)
                } catch (error: Exception) {
                    showSnackbar("Error: Unable to update quote, ${error.message}")
                    return
                }
                showSnackbar("Success: Quote successfully updated!")
                quotes.value = appCore.getQuotes()
                focusRequester.requestFocus()
            }

            fun deleteQuote(quoteId: Int) {
                try {
                    appCore.deleteQuote(quoteId)
                } catch (error: Exception) {
                    showSnackbar("Error: Unable to delete quote, ${error.message}")
                    return
                }
                showSnackbar("Success: Quote successfully deleted!")
                quotes.value = appCore.getQuotes()
            }

            val tags = remember { mutableStateOf(appCore.getTags()) }
            fun addTagInModal(tag: Tag) {
                try {
                    appCore.addTag(tag)
                } catch (error: Exception) {
                    showSnackbar("Error: Unable to add tag, ${error.message}")
                    return
                }
                showSnackbar("Success: Tag successfully added!")
                tags.value = appCore.getTags()
            }

            fun updateTagForQuotes(tagId: Int, newName: String) {
                quotes.value = quotes.value.map {
                    quote -> quote.copy(tags = quote.tags.map {
                        tag ->
                        if (tag.id == tagId) {
                            tag.copy(name = newName)
                        } else {
                            tag
                        }
                })
                }
            }
            fun updateTagInModal(tagId: Int, newName: String) {
                try {
                    appCore.updateTag(tagId, newName)
                } catch (error: Exception) {
                    showSnackbar("Error: Unable to update tag, ${error.message}")
                    return
                }
                showSnackbar("Success: Tag successfully updated!")
                tags.value = appCore.getTags()
                updateTagForQuotes(tagId, newName)
            }

            fun removeTagFromQuotes(tagId: Int) {
                quotes.value = quotes.value.map {
                        quote -> quote.copy(tags = quote.tags.filterNot {
                        tag ->
                        tag.id == tagId
                })
                }
            }
            fun deleteTagInModal(tagId: Int) {
                try {
                    appCore.deleteTag(tagId)
                } catch (error: Exception) {
                    showSnackbar("Error: Unable to delete tag, ${error.message}")
                    return
                }
                showSnackbar("Success: Tag successfully deleted!")
                tags.value = appCore.getTags()
                removeTagFromQuotes(tagId)
            }


            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    SearchBar(
                        ::updateSearchTerm,
                        Modifier.padding(12.dp, 12.dp, 12.dp, 0.dp).width(600.dp).focusRequester(focusRequester)
                            .moveFocusOnTab()
                    )
                    BadgedBox(
                        badge = {
                            if (tagFilters.value.isNotEmpty()) {
                                Badge(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                ) {
                                    Text(tagFilters.value.size.toString())
                                }
                            }
                        },
                        modifier = Modifier.padding(top = 12.dp, start = 8.dp)
                    ) {
                        Button(
                            onClick = {
                                openFilterQuotesModal.value = true
                            },
                            colors = ButtonColors(
                                containerColor = Color.LightGray,
                                contentColor = Color.White,
                                disabledContainerColor = Color.LightGray,
                                disabledContentColor = Color.Gray
                            ),
                            modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                        ) {
                            Icon(Icons.Default.Tune, contentDescription = "filter")
                            Text(" Filter", color = Color.White, fontSize = 24.sp)
                        }
                    }

                    Button(
                        onClick = {
                            openAddQuoteModal.value = true
                        },
                        colors = ButtonColors(
                            containerColor = Color(23, 176, 71),
                            contentColor = Color.White,
                            disabledContainerColor = Color(23, 176, 71),
                            disabledContentColor = Color.Gray
                        ),
                        content = {
                            Icon(Icons.AutoMirrored.Default.NoteAdd, contentDescription = "add quote")
                            Text(" Add Quote", color = Color.White, fontSize = 24.sp)
                        },
                        modifier = Modifier.padding(top = 12.dp, start = 680.dp).pointerHoverIcon(PointerIcon.Hand)
                    )


                    Button(
                        onClick = {
                            openManageTagsModal.value = true
                        },
                        colors = ButtonColors(
                            containerColor = Color(52, 161, 235),
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.Gray
                        ),
                        modifier = Modifier.padding(top = 12.dp, start = 8.dp).pointerHoverIcon(PointerIcon.Hand)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "manage tags")
                        Text(" Manage Tags", color = Color.White, fontSize = 24.sp)
                    }
                }
                QuoteTable(
                    filteredQuotes,
                    { q ->
                        editQuote.value = q
                        openEditQuotesModal.value = true
                    },
                    ::deleteQuote,
                    ::showSnackbar,
                    Modifier.fillMaxSize().padding(12.dp, 12.dp, 12.dp, 12.dp)
                )
            }

            if (openAddQuoteModal.value) {
                AddQuoteModal(::addQuoteInModal, tags.value, ::hideAddQuoteModal)
            }

            if (openFilterQuotesModal.value) {
                FilterQuotesModal(
                    tags.value,
                    tagFilters.value,
                    ::filterQuotesByTags,
                    ::addTagInModal,
                    ::hideFilterQuotesModal
                )
            }

            if (openEditQuotesModal.value && editQuote.value != null) {
                EditQuoteModal(editQuote.value!!, ::updateQuoteInModal, tags.value, ::hideEditQuotesModal)
            }

            if (openManageTagsModal.value) {
                ManageTagsModal(tags.value,::addTagInModal, ::updateTagInModal, ::deleteTagInModal, ::hideManageTagsModal)
            }
        }
    }
}
