package org.example.quotes.selectedTags

import Tag
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SelectedTags(tags: List<Tag>) {
    FlowRow(maxItemsInEachRow = 2) {
        tags.forEach { tag ->
            Text(tag.name)
        }

    }
}
