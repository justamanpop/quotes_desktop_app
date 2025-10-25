package org.example.quotes

import QuoteCore
import QuoteTable
import SearchBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.util.Locale.getDefault
import kotlin.collections.filter

@Composable
@Preview
fun App(quoteCore: QuoteCore) {
    MaterialTheme {
        var currentSearchTerm = remember { mutableStateOf("") }
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


        Column(modifier = Modifier.fillMaxSize()) {
            SearchBar(::updateSearchTerm, Modifier.padding(12.dp, 12.dp, 12.dp, 0.dp).width(600.dp))
            QuoteTable(quotes.value, Modifier.fillMaxSize().padding(12.dp, 12.dp, 12.dp, 12.dp))
        }
    }
}
