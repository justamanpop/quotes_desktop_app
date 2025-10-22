package org.example.quotes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import quotes.composeapp.generated.resources.Res
import quotes.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(12.dp, 12.dp, 12.dp, 12.dp).border(1.dp, DividerDefaults.color)) {
            fakeQuotes().forEach { quote ->
                item {
                    Text(quote.first, fontSize = 24.sp, lineHeight = 32.sp, modifier = Modifier.padding(8.dp))
                    Text(quote.second, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
                    HorizontalDivider()
                }
            }
        }
    }
}