package org.example.quotes.filterQuotesModal

import Tag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import moveFocusOnTab
import org.example.quotes.selectedTags.SelectedTags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterQuotesModal(tags: List<Tag>, onDismissRequest: () -> Unit) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val selectedTags = remember { mutableStateOf(listOf<Tag>()) }

    val (selectedTag, setSelectedTag) = remember { mutableStateOf<Tag?>(null) }

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
    ) {
        Card(modifier = Modifier.width(660.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(16.dp).fillMaxWidth().moveFocusOnTab()
            ) {
                fun getTagDisplay(tag: Tag): String {
                    return tag.name
                }

                Row {
                    SearchableDropdown(tags, ::getTagDisplay, setSelectedTag)
                    Button(
                        content = { Text("+") }, onClick = {
                            selectedTag?.let {
                                selectedTags.value += selectedTag
                            }
                        }, colors = ButtonColors(
                            containerColor = Color(52, 161, 235),
                            contentColor = Color.White,
                            disabledContainerColor = Color(23, 176, 71),
                            disabledContentColor = Color.Gray
                        ),
                        modifier = Modifier.padding(start = 24.dp)
                    )
                    SelectedTags(selectedTags.value)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(content = { Text("Filter") }, onClick = {
                        onDismissRequest()
                    })
                    Button(content = { Text("Close") }, onClick = { onDismissRequest() })
                }
            }
        }
    }
}