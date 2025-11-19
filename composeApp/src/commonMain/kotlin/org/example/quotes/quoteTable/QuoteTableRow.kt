package org.example.quotes.quoteTable

import Quote
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.quotes.shared.lightBorderIfFocused

@Composable
fun QuoteTableRow(
    quote: Quote,
    onRowClick: (Quote) -> Unit,
    showSnackbar: (message: String) -> Unit,
    copyToClipboard: (String) -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(modifier = Modifier.clickable(enabled = true, onClick = {
        onRowClick(quote)
    }).pointerHoverIcon(PointerIcon.Hand)) {
        Column(modifier = Modifier.weight(14f)) {
            Text(
                quote.content,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                modifier = Modifier.padding(8.dp)
            )
            Row() {
                Text(
                    quote.source,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp, end = 24.dp).align(Alignment.Bottom),
                )
                quote.tags.forEach { tag ->
                    Card(
                        colors = CardColors(
                            containerColor = Color.LightGray,
                            contentColor = Color(64, 126, 201),
                            disabledContainerColor = Color.LightGray,
                            disabledContentColor = Color.White,
                        ),
                        modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                    ) {
                        Text(
                            tag.name,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        Row(
            Modifier.weight(1f).fillMaxHeight().padding(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            var isCopyQuoteButtonFocused by remember { mutableStateOf(false) }
            Button(
                onClick = {
                    copyToClipboard(quote.content)
                    showSnackbar("Info: Quote copied!")
                },
                colors = ButtonColors(
                    contentColor = Color.White,
                    containerColor = if (isCopyQuoteButtonFocused) Color.Gray else Color.LightGray,
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color.LightGray
                ),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.padding(top = 8.dp, end = 8.dp)
                    .pointerHoverIcon(
                        PointerIcon.Hand
                    )
                    .onFocusChanged { focusState ->
                        isCopyQuoteButtonFocused = focusState.isFocused
                    }
                    .lightBorderIfFocused(isCopyQuoteButtonFocused, 2.dp)
                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
            ) {
                Icon(
                    Icons.Default.ContentCopy,
                    contentDescription = "copy",
                    modifier = Modifier.height(24.dp).width(24.dp)
                )
            }

            var isDeleteButtonFocused by remember { mutableStateOf(false) }
            Button(
                onClick = onDeleteClick,
                colors = ButtonColors(
                    contentColor = Color.White,
                    containerColor = if (isDeleteButtonFocused) Color(
                        156,
                        3,
                        3,
                        255
                    ) else Color(201, 9, 35, 255),
                    disabledContentColor = Color.White,
                    disabledContainerColor = Color(186, 22, 39),
                ),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.padding(top = 8.dp, end = 16.dp)
                    .pointerHoverIcon(
                        PointerIcon.Hand
                    )
                    .onFocusChanged { focusState -> isDeleteButtonFocused = focusState.isFocused }
                    .lightBorderIfFocused(isDeleteButtonFocused, 2.dp)
                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "delete",
                    modifier = Modifier.height(24.dp).width(24.dp)
                )
            }
        }
    }
}