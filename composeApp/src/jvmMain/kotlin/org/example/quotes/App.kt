package org.example.quotes

import Quote
import QuoteCore
import QuoteTable
import SearchBar
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
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
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moveFocusOnTab
import org.example.quotes.addQuoteModal.AddQuoteModal
import java.util.Locale.getDefault
import kotlin.collections.filter

@Composable
fun App(quoteCore: QuoteCore) {
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
                        Snackbar(snackbarData)
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

            val quotes = remember { mutableStateOf(quoteCore.getQuotes()) }
            fun updateSearchTerm(newVal: String) {
                currentSearchTerm.value = newVal
                quotes.value = quoteCore.getQuotes().filter { q ->
                    val filterTerm = newVal.lowercase(getDefault())
                    val lowerCaseContent = q.content.lowercase(getDefault())
                    val lowerCaseSource = q.source.lowercase(getDefault())

                    lowerCaseContent.contains(filterTerm) || lowerCaseSource.contains(filterTerm)
                }
            }

            fun addQuoteInModal(quote: Quote) {
                quoteCore.addQuote(quote)
                showSnackbar("Quote successfully added!")
                quotes.value = quoteCore.getQuotes()
                focusRequester.requestFocus()
            }

            fun deleteQuote(quoteId: Int) {
                quoteCore.deleteQuote(quoteId)
                val temp = quoteCore.getQuotes()
                println(temp.map { q -> q.id })
                quotes.value = temp
            }


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
                            containerColor = Color.Green,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Green,
                            disabledContentColor = Color.Gray
                        ),
                        content = {
                            Icon(Icons.AutoMirrored.Default.NoteAdd, contentDescription = "")
                            Text(" Add Quote", color = Color.White, fontSize = 24.sp)
                        },
                        modifier = Modifier.padding(top = 12.dp).pointerHoverIcon(PointerIcon.Hand)
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
        }

    }
}
