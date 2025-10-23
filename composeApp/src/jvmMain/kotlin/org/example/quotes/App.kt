package org.example.quotes

import QuoteTable
import SearchBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
           SearchBar(Modifier.padding(12.dp, 12.dp, 12.dp, 0.dp).width(600.dp))
           QuoteTable(Modifier.fillMaxSize().padding(12.dp, 12.dp, 12.dp, 12.dp))
        }

    }
}