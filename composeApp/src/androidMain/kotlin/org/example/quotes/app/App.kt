package org.example.quotes.app

import QuoteTable
import SearchBar
import android.content.ClipData
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterAlt
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.quotes.shared.constructSnackbarDataObject
import org.example.quotes.shared.getSnackbarColor
import org.example.quotes.modals.quoteEditorModal.QuoteEditorModal
import org.example.quotes.modals.FilterQuotesModal
import org.example.quotes.modals.ManageTagsModal
import org.example.quotes.modals.quoteEditorModal.QuoteEditorMode
import org.example.quotes.shared.lightBorderIfFocused
import org.example.quotes.shared.moveFocusOnTab
import org.example.quotes.shared.stripSnackbarMessage

@Composable
actual fun App(viewModel: AppViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val filteredQuotes by viewModel.filteredQuotes.collectAsStateWithLifecycle()

    val clipboard = LocalClipboard.current
    fun copyToClipboard(text: String) {
        clipboard.nativeClipboard.setPrimaryClip(ClipData.newPlainText("", text))
    }

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
        ) { scaffoldPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
            ) {
                SearchBar(
                    viewModel::updateSearchTerm,
                    Modifier
                        .padding(12.dp, 12.dp, 12.dp, 0.dp)
                        .fillMaxWidth()
                        .moveFocusOnTab()
                )
                Row(modifier = Modifier.fillMaxWidth()) {
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
                        var isTagFilterButtonFocused by remember { mutableStateOf(false) }
                        Button(
                            onClick = {
                                viewModel.showFilterQuotesModal()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isTagFilterButtonFocused) Color(0xFF767272) else Color.LightGray,
                                contentColor = Color.White,
                                disabledContainerColor = Color.LightGray,
                                disabledContentColor = Color.Gray
                            ),
                            modifier = Modifier
                                .pointerHoverIcon(PointerIcon.Hand)
                                .onFocusChanged { focusState -> isTagFilterButtonFocused = focusState.isFocused }
                                .lightBorderIfFocused(isTagFilterButtonFocused)
                        ) {
                            Icon(Icons.Default.FilterAlt, contentDescription = "filter")
                            Text(" Tag Filter", color = Color.White, fontSize = 16.sp)
                        }
                    }


                    var isManageTagsButtonFocused by remember { mutableStateOf(false) }
                    Button(
                        onClick = {
                            viewModel.showManageTagsModal()
                        },
                        colors = ButtonColors(
                            containerColor = if (isManageTagsButtonFocused) Color(15, 81, 186) else Color(52, 161, 235),
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.Gray
                        ),
                        modifier = Modifier
                            .padding(top = 12.dp, start = 8.dp)
                            .pointerHoverIcon(PointerIcon.Hand)
                            .onFocusChanged { focusState -> isManageTagsButtonFocused = focusState.isFocused }
                            .lightBorderIfFocused(isManageTagsButtonFocused)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "manage tags")
                        Text(" Manage Tags", color = Color.White, fontSize = 16.sp)
                    }
                }

                Spacer(Modifier.height(12.dp))
                var isAddQuoteButtonFocused by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        viewModel.showAddQuoteModal()
                    },
                    colors = ButtonColors(
                        containerColor = if (isAddQuoteButtonFocused) Color(3, 104, 3, 255) else Color(23, 176, 71),
                        contentColor = Color.White,
                        disabledContainerColor = Color(23, 176, 71),
                        disabledContentColor = Color.Gray
                    ),
                    content = {
                        Icon(Icons.AutoMirrored.Default.NoteAdd, contentDescription = "add quote")
                        Text(" Add Quote", color = Color.White, fontSize = 16.sp)
                    },
                    modifier = Modifier
                        .padding(top = 12.dp, start = 8.dp)
                        .pointerHoverIcon(PointerIcon.Hand)
                        .onFocusChanged { focusState -> isAddQuoteButtonFocused = focusState.isFocused }
                        .lightBorderIfFocused(isAddQuoteButtonFocused)
                )
                QuoteTable(
                    filteredQuotes,
                    { q ->
                        viewModel.showEditQuoteModal(q)
                    },
                    viewModel::deleteQuote,
                    viewModel::showSnackbarMessage,
                    ::copyToClipboard,
                    Modifier
                        .fillMaxSize()
                        .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                )
            }

            if (state.isFilterQuotesModalOpen) {
                FilterQuotesModal(
                    state.tags,
                    state.filterTags,
                    viewModel::updateFilterTags,
                    viewModel::hideFilterQuotesModal
                )
            }

            if (state.isAddQuoteModalOpen) {
                QuoteEditorModal(
                    QuoteEditorMode.AddMode(viewModel::addQuote),
                    viewModel::addTag,
                    state.tags,
                    viewModel::hideAddQuoteModal
                )
            }

            val quoteToEdit = state.quoteClickedForEdit
            if (state.isEditQuoteModalOpen && quoteToEdit != null) {
                QuoteEditorModal(
                    QuoteEditorMode.EditMode(quoteToEdit, viewModel::updateQuote),
                    viewModel::addTag,
                    state.tags,
                    viewModel::hideEditQuoteModal,
                )
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
