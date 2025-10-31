package org.example.quotes.selectedTags

import Tag
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SelectedTags(tags: List<Tag>) {
   tags.forEach {
       tag ->
       Text(tag.name)
   }
}
