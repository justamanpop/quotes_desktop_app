package org.example.quotes.app

import QuoteTable
import SearchBar
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import constructSnackbarDataObject
import getSnackbarColor
import moveFocusOnTab
import org.example.quotes.quoteEditorModal.QuoteEditorModal
import org.example.quotes.filterQuotesModal.FilterQuotesModal
import org.example.quotes.manageTagsModal.ManageTagsModal
import org.example.quotes.quoteEditorModal.QuoteEditorMode
import stripSnackbarMessage

@Composable
fun App(viewModel: AppViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val filteredQuotes by viewModel.filteredQuotes.collectAsStateWithLifecycle()

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        viewModel.focusRequest.collect {
            focusRequester.requestFocus()
        }
    }

    val snackbarState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { message ->
            snackbarState.showSnackbar(message)
        }
    }

    MaterialTheme {
        Scaffold(
            snackbarHost = {
                    SnackbarHost(hostState = snackbarState, snackbar = { snackbarData ->
                        val containerColor = getSnackbarColor(snackbarData.visuals.message)
                        val updatedSnackbarData =
                            constructSnackbarDataObject(stripSnackbarMessage(snackbarData.visuals.message))

                        Snackbar(updatedSnackbarData, containerColor = containerColor)
                    })
            }
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    SearchBar(
                        viewModel::updateSearchTerm,
                        Modifier.padding(12.dp, 12.dp, 12.dp, 0.dp).width(600.dp).focusRequester(focusRequester)
                            .moveFocusOnTab()
                    )
                    BadgedBox(
                        badge = {
                            if (state.filterTags.isNotEmpty()) {
                                Badge(
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                ) {
                                    Text(state.filterTags.size.toString())
                                }
                            }
                        },
                        modifier = Modifier.padding(top = 12.dp, start = 8.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.showFilterQuotesModal()
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
                            viewModel.showAddQuoteModal()
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
                            viewModel.showManageTagsModal()
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
                        viewModel.showEditQuoteModal(q)
                    },
                    viewModel::deleteQuote,
                    viewModel::showSnackbarMessage,
                    Modifier.fillMaxSize().padding(12.dp, 12.dp, 12.dp, 12.dp)
                )
            }

            if (state.isFilterQuotesModalOpen) {
                FilterQuotesModal(
                    state.tags,
                    state.filterTags,
                    viewModel::updateFilterTags,
                    viewModel::addTag,
                    viewModel::hideFilterQuotesModal
                )
            }

            if (state.isAddQuoteModalOpen) {
                QuoteEditorModal(QuoteEditorMode.AddMode(viewModel::addQuote), state.tags, viewModel::hideAddQuoteModal)
            }

            val quoteToEdit = state.quoteClickedForEdit
            if (state.isEditQuoteModalOpen && quoteToEdit != null) {
                QuoteEditorModal(QuoteEditorMode.EditMode(quoteToEdit, viewModel::updateQuote),  state.tags, viewModel::hideEditQuoteModal)
            }

            if (state.isManageTagsModalOpen) {
                ManageTagsModal(
                    state.tags,
                    viewModel::addTag,
                    viewModel::updateTagName,
                    viewModel::deleteTag,
                    viewModel::hideManageTagsModal
                )
            }
        }
    }
}
