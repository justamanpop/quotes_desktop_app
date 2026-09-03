package org.example.quotes.quoteTable

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PlatformVerticalScrollbar(modifier: Modifier, scrollState: LazyListState)
