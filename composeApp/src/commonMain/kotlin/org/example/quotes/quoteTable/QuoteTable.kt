import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.quotes.quoteTable.QuoteTableRow
import org.example.quotes.shared.DeleteConfirmationModal
import org.example.quotes.shared.lightBorderIfFocused
import kotlin.collections.forEach

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
                .border(1.dp, DividerDefaults.color), state
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
    }
    if (openDeleteQuoteConfirmationModal.value) {
        DeleteConfirmationModal(quoteIdToDelete.value, deleteQuote, "quote", ::hideDeleteQuoteConfirmationModal)
    }
}