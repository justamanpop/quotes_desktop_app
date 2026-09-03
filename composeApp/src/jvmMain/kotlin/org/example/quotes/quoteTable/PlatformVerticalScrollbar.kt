package org.example.quotes.quoteTable

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun PlatformVerticalScrollbar(modifier: Modifier, scrollState: LazyListState) {
   VerticalScrollbar(rememberScrollbarAdapter(scrollState), modifier)
}
