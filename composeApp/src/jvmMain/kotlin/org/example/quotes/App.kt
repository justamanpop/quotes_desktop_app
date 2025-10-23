package org.example.quotes

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search

@Composable
@Preview
fun App() {
    var searchText = remember { mutableStateOf("") }
    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            TextField(
                value = searchText.value,
                onValueChange = { v ->
                    searchText.value = v
                },
                placeholder = {Text("Search")},
                leadingIcon = { Icon(Icons.Default.Search, "search") },
                modifier = Modifier.padding(12.dp, 12.dp, 12.dp, 0.dp).width(600.dp)
            )
            Box(modifier = Modifier.fillMaxSize().padding(12.dp, 12.dp, 12.dp, 12.dp)) {
                val state = rememberLazyListState()
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .border(1.dp, DividerDefaults.color), state
                ) {
                    fakeQuotes().forEach { quote ->
                        item {
                            Text(quote.first, fontSize = 24.sp, lineHeight = 32.sp, modifier = Modifier.padding(8.dp))
                            Text(
                                quote.second,
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

    }
}