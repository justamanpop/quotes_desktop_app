import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Composable
fun QuoteTable(quotes: List<Quote>, deleteQuote: (quoteId: Int) -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        val state = rememberLazyListState()

        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .border(1.dp, DividerDefaults.color), state
        ) {
            quotes.forEach { quote ->
                item {
                    Row(modifier = Modifier.clickable(enabled = true, onClick = {
                        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                        val stringSelection = StringSelection(quote.content)
                        clipboard.setContents(stringSelection, null)
                    }).pointerHoverIcon(PointerIcon.Hand).height(IntrinsicSize.Min)) {
                        Column(modifier = Modifier.weight(16f)) {
                            Text(quote.content, fontSize = 24.sp, lineHeight = 32.sp, modifier = Modifier.padding(8.dp))
                            Text(
                                quote.source,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Row(Modifier.weight(1f).fillMaxHeight().padding(2.dp), verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = {
                                    deleteQuote(quote.id)
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
                                Icon(Icons.Default.Delete, contentDescription = "delete", modifier = Modifier.height(24.dp).width(24.dp))
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
}