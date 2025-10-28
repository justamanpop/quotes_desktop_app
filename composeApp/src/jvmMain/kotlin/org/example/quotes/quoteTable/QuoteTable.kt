import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable

@Composable
fun QuoteTable(quotes: List<Quote>, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        val state = rememberLazyListState()

        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .border(1.dp, DividerDefaults.color), state
        ) {
            quotes.forEach { quote ->
                item {
                    Row {
                        Column(modifier = Modifier.weight(16f)) {
                            Text(quote.content, fontSize = 24.sp, lineHeight = 32.sp, modifier = Modifier.padding(8.dp))
                            Text(
                                quote.source,
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Button(
                            onClick = {
                                val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                                val stringSelection = StringSelection(quote.content)
                                clipboard.setContents(stringSelection, null)
                            },
                            modifier = Modifier.weight(1f).padding(top = 8.dp, end = 16.dp).width(2.dp)
                                .pointerHoverIcon(
                                    PointerIcon.Hand
                                )
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "copy")
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