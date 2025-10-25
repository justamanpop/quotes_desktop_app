import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
                    Text(quote.content, fontSize = 24.sp, lineHeight = 32.sp, modifier = Modifier.padding(8.dp))
                    Text(
                        quote.source,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 8.dp)
                    )
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