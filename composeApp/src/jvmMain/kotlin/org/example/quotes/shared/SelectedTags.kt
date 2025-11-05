package org.example.quotes.shared

import Tag
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp

@Composable
fun SelectedTags(tags: Set<Tag>, removeTag: (Tag) -> Unit, modifier: Modifier = Modifier) {
    FlowRow(maxItemsInEachRow = 2, verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        tags.forEach { tag ->
            var isTagFocused by remember { mutableStateOf(false) }
            Card(
                colors = CardColors(
                    containerColor = if (isTagFocused) Color.Gray else Color.LightGray,
                    contentColor = Color(64, 126, 201),
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.White,
                ),
                modifier = Modifier.padding(start = 8.dp)
                    .pointerHoverIcon(PointerIcon.Hand)
                    .onFocusChanged { focusState -> isTagFocused = focusState.isFocused }
                    .lightBorderIfFocused(isTagFocused, 2.dp)
                    .clickable(enabled = true, onClick = {
                        removeTag(tag)
                    })
            ) {
                Text(tag.name, modifier = Modifier.padding(8.dp))
            }
        }

    }
}
