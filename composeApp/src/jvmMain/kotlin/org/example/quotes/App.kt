package org.example.quotes

import Quote
import AppCore
import QuoteTable
import SearchBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.Tune
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
import constructSnackbarDataObject
import getSnackbarColor
import kotlinx.coroutines.launch
import moveFocusOnTab
import org.example.quotes.addQuoteModal.AddQuoteModal
import org.example.quotes.filterQuotesModal.FilterQuotesModal
import stripSnackbarMessage
import java.util.Locale.getDefault
import kotlin.collections.filter

@Composable
fun App(appCore: AppCore) {
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
                    }, modifier = Modifier.align(Alignment.TopCenter))
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

            val quotes = remember { mutableStateOf(appCore.getQuotes()) }
            fun updateSearchTerm(newVal: String) {
                currentSearchTerm.value = newVal
                quotes.value = appCore.getQuotes().filter { q ->
                    val filterTerm = newVal.lowercase(getDefault())
                    val lowerCaseContent = q.content.lowercase(getDefault())
                    val lowerCaseSource = q.source.lowercase(getDefault())

                    lowerCaseContent.contains(filterTerm) || lowerCaseSource.contains(filterTerm)
                }
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


            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    SearchBar(
                        ::updateSearchTerm,
                        Modifier.padding(12.dp, 12.dp, 12.dp, 0.dp).width(600.dp).focusRequester(focusRequester)
                            .moveFocusOnTab()
                    )
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
                            Icon(Icons.AutoMirrored.Default.NoteAdd, contentDescription = "add")
                            Text(" Add", color = Color.White, fontSize = 24.sp)
                        },
                        modifier = Modifier.padding(top = 12.dp).pointerHoverIcon(PointerIcon.Hand)
                    )

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
                        content = {
                            Icon(Icons.Default.Tune, contentDescription = "filter")
                            Text(" Filter", color = Color.White, fontSize = 24.sp)
                        },
                        modifier = Modifier.padding(top = 12.dp, start = 8.dp).pointerHoverIcon(PointerIcon.Hand)
                    )
                }
                QuoteTable(
                    quotes.value,
                    ::deleteQuote,
                    ::showSnackbar,
                    Modifier.fillMaxSize().padding(12.dp, 12.dp, 12.dp, 12.dp)
                )
            }

            if (openAddQuoteModal.value) {
                AddQuoteModal(::addQuoteInModal, ::hideAddQuoteModal)
            }

            if (openFilterQuotesModal.value) {
                FilterQuotesModal(tags.value, ::hideFilterQuotesModal)
            }
        }
    }
}
