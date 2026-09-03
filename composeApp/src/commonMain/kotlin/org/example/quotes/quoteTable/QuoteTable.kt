package org.example.quotes.quoteTable

import Quote
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.quotes.shared.DeleteConfirmationModal

@Composable
fun QuoteTable(
    quotes: List<Quote>,
    onRowClick: (Quote) -> Unit,
    deleteQuote: (quoteId: Int) -> Unit,
    showSnackbar: (message: String) -> Unit,
    copyToClipboard: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val openDeleteQuoteConfirmationModal = remember { mutableStateOf(false) }
    fun hideDeleteQuoteConfirmationModal() {
        openDeleteQuoteConfirmationModal.value = false
    }

    val quoteIdToDelete: MutableState<Int?> = remember { mutableStateOf(null) };

    Box(modifier = modifier) {
        val state = rememberLazyListState()
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .border(1.dp, DividerDefaults.color),
            state
        ) {
            quotes.forEach { quote ->
                item {
                    QuoteTableRow(quote, onRowClick, showSnackbar, copyToClipboard, {
                        quoteIdToDelete.value = quote.id
                        openDeleteQuoteConfirmationModal.value = true
                    })
                    HorizontalDivider()
                }
            }
        }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.TopEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(state)
        )
    }
    if (openDeleteQuoteConfirmationModal.value) {
        DeleteConfirmationModal(quoteIdToDelete.value, deleteQuote, "quote", ::hideDeleteQuoteConfirmationModal)
    }
}
