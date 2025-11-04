package org.example.quotes.tagEditorModal

import Tag
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import moveFocusOnTab

@Composable
fun TagEditorModal(tagEditorMode: TagEditorMode, onDismissRequest: () -> Unit) {
    val tagName = remember {
        mutableStateOf(
            when (tagEditorMode) {
                is TagEditorMode.AddMode -> {
                    ""
                }
                is TagEditorMode.EditMode -> {
                    tagEditorMode.tag.name
                }
            }
        )
    }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

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
                OutlinedTextField(
                    value = tagName.value,
                    onValueChange = { v -> tagName.value = v },
                    label = { Text("Tag Name") },
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester).moveFocusOnTab()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = {
                        when (tagEditorMode) {
                            is TagEditorMode.AddMode -> {
                                tagEditorMode.addTag(Tag(-1, tagName.value))
                            }
                            is TagEditorMode.EditMode -> {
                                tagEditorMode.updateTag(tagEditorMode.tag.id, tagName.value)
                            }
                        }
                        onDismissRequest()
                    }) {
                        val buttonText = when (tagEditorMode) {
                            is TagEditorMode.AddMode -> {
                                "Create Tag"
                            }

                            is TagEditorMode.EditMode -> {
                                "Update Tag"
                            }
                        }
                        Text(buttonText)
                    }
                    Button(onClick = { onDismissRequest() }) {
                        Text("Close")
                    }
                }
            }
        }
    }
}