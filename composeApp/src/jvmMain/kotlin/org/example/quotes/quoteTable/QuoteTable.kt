import androidx.compose.foundation.VerticalScrollbar
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
import androidx.compose.foundation.rememberScrollbarAdapter
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.quotes.deleteConfirmationModal.DeleteConfirmationModal
import kotlin.collections.forEach

@Composable
fun QuoteTable(
    quotes: List<Quote>,
    onRowClick: (Quote) -> Unit,
    deleteQuote: (quoteId: Int) -> Unit,
    showSnackbar: (message: String) -> Unit,
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
                    Row(modifier = Modifier.clickable(enabled = true, onClick = {
                        onRowClick(quote)
                    }).pointerHoverIcon(PointerIcon.Hand).height(IntrinsicSize.Min)) {
                        Column(modifier = Modifier.weight(14f)) {
                            Text(quote.content, fontSize = 24.sp, lineHeight = 32.sp, modifier = Modifier.padding(8.dp))
                            Row {
                                Text(
                                    quote.source,
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                                quote.tags.forEach { tag ->
                                    Card(
                                        colors = CardColors(
                                            containerColor = Color.LightGray,
                                            contentColor = Color(64, 126, 201),
                                            disabledContainerColor = Color.LightGray,
                                            disabledContentColor = Color.White,
                                        ),
                                        modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                                    ) {
                                        Text(tag.name, fontSize = 12.sp, modifier = Modifier.padding(2.dp))
                                    }
                                }
                            }
                        }

                        Row(
                            Modifier.weight(1f).fillMaxHeight().padding(2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    copyToClipboard(quote.content)
                                    showSnackbar("Info: Quote copied!")
                                },
                                colors = ButtonColors(
                                    contentColor = Color.White,
                                    containerColor = Color.LightGray,
                                    disabledContentColor = Color.White,
                                    disabledContainerColor = Color.LightGray
                                ),
                                contentPadding = PaddingValues(8.dp),
                                modifier = Modifier.padding(top = 8.dp, end = 8.dp)
                                    .pointerHoverIcon(
                                        PointerIcon.Hand
                                    ).defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                            ) {
                                Icon(
                                    Icons.Default.ContentCopy,
                                    contentDescription = "copy",
                                    modifier = Modifier.height(24.dp).width(24.dp)
                                )
                            }
                            Button(
                                onClick = {
                                    quoteIdToDelete.value = quote.id
                                    openDeleteQuoteConfirmationModal.value = true

                                },
                                colors = ButtonColors(
                                    contentColor = Color.White,
                                    containerColor = Color(186, 22, 39),
                                    disabledContentColor = Color.White,
                                    disabledContainerColor = Color(186, 22, 39),
                                ),
                                contentPadding = PaddingValues(8.dp),
                                modifier = Modifier.padding(top = 8.dp, end = 16.dp)
                                    .pointerHoverIcon(
                                        PointerIcon.Hand
                                    ).defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "delete",
                                    modifier = Modifier.height(24.dp).width(24.dp)
                                )
                            }
                        }
                    }
                    HorizontalDivider()
                }
            }
        }

        VerticalScrollbar(
            modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd),
            adapter = rememberScrollbarAdapter(state)
        )
    }
    if (openDeleteQuoteConfirmationModal.value) {
        DeleteConfirmationModal(quoteIdToDelete.value, deleteQuote, "quote", ::hideDeleteQuoteConfirmationModal)
    }
}