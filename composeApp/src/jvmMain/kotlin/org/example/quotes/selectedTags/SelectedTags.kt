package org.example.quotes.selectedTags

import Tag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SelectedTags(tags: Set<Tag>, modifier: Modifier = Modifier) {
    FlowRow(maxItemsInEachRow = 2, verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        tags.forEach { tag ->
            Card(
                colors = CardColors(
                    containerColor = Color.LightGray,
                    contentColor = Color(64, 126, 201),
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.White,
                ),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(tag.name, modifier = Modifier.padding(8.dp))
            }
        }

    }
}
