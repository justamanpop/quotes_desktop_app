package org.example.quotes.modals.tagEditorModal

import Tag
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.example.quotes.shared.moveFocusOnTab

@Composable
fun TagEditorModal(tagEditorMode: TagEditorMode, onDismissRequest: () -> Unit) {
    var tagNameTextFieldValue by remember {
        val initialTagName = tagEditorMode.getInitialTagName()
        mutableStateOf(
            TextFieldValue(
                text = initialTagName,
                selection = TextRange(initialTagName.length)
            )
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
                    value = tagNameTextFieldValue,
                    onValueChange = { v -> tagNameTextFieldValue = v },
                    label = { Text("Tag Name") },
                    modifier = Modifier.fillMaxWidth().focusRequester(focusRequester).moveFocusOnTab()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = {
                            tagEditorMode.performAction(Tag(-1, tagNameTextFieldValue.text))
                            onDismissRequest()
                        },
                        colors = ButtonColors(
                            containerColor = Color(23, 176, 71),
                            contentColor = Color.White,
                            disabledContainerColor = Color(23, 176, 71),
                            disabledContentColor = Color.Gray
                        ),
                    ) {
                        Text(tagEditorMode.getButtonText())
                    }
                    Button(
                        onClick = { onDismissRequest() },
                        colors = ButtonColors(
                            contentColor = Color.White,
                            containerColor = Color(201, 9, 35, 255),
                            disabledContentColor = Color.White,
                            disabledContainerColor = Color(186, 22, 39),
                        ),
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}