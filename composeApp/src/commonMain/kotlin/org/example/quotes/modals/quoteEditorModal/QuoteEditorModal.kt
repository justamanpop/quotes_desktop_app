package org.example.quotes.modals.quoteEditorModal

import Quote
import Tag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.example.quotes.modals.tagEditorModal.TagEditorModal
import org.example.quotes.modals.tagEditorModal.TagEditorMode
import org.example.quotes.shared.TagSearchableDropdown
import org.example.quotes.shared.SelectedTags
import org.example.quotes.shared.lightBorderIfFocused
import org.example.quotes.shared.moveFocusOnTab

@Composable
fun QuoteEditorModal(
    quoteEditorMode: QuoteEditorMode,
    addTag: (Tag) -> Unit,
    tags: List<Tag>,
    onDismissRequest: () -> Unit
) {
    var contentText by remember {
        val content = quoteEditorMode.getInitialContent()
        mutableStateOf(TextFieldValue(content, TextRange(content.length)))
    }
    var sourceText by remember {
        val source = quoteEditorMode.getInitialSource()
        mutableStateOf(TextFieldValue(source, TextRange(source.length)))
    }

    var isContentError by remember { mutableStateOf(false) }
    var isSourceError by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }


    val (dropdownInputValue, setDropdownInputValue) = remember { mutableStateOf("") }
    var dropdownTextFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = dropdownInputValue,
                selection = TextRange(dropdownInputValue.length)
            )
        )
    }

    fun setDropdownTextFieldState(value: TextFieldValue) {
        dropdownTextFieldValue = value
    }

    val selectedTags = remember { mutableStateOf(quoteEditorMode.quote?.tags?.toSet() ?: setOf()) }
    fun unselectTag(tag: Tag) {
        selectedTags.value = selectedTags.value.minus(tag)
    }

    val inputFieldFocusRequester = remember { FocusRequester() }
    fun selectTagFilter(tag: Tag) {
        selectedTags.value = selectedTags.value.plus(tag)
        setDropdownInputValue("")
        dropdownTextFieldValue = TextFieldValue("")
        inputFieldFocusRequester.requestFocus()
    }

    val openAddTagModal = remember { mutableStateOf(false) }
    fun hideAddTagModal() {
        openAddTagModal.value = false
        inputFieldFocusRequester.requestFocus()
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
                    value = contentText,
                    onValueChange = {
                        contentText = it
                        isContentError = contentText.text.isEmpty()
                    },
                    isError = isContentError,
                    label = { Text("Content") },
                    modifier = Modifier.height(220.dp).fillMaxWidth().focusRequester(focusRequester).moveFocusOnTab()
                )
                OutlinedTextField(
                    value = sourceText,
                    onValueChange = {
                        sourceText = it
                        isSourceError = sourceText.text.isEmpty()
                    },
                    isError = isSourceError,
                    label = { Text("Source") },
                    modifier = Modifier.fillMaxWidth()
                )


                Row {
                    TagSearchableDropdown(
                        tags.minus(selectedTags.value),
                        dropdownTextFieldValue,
                        ::setDropdownTextFieldState,
                        setDropdownInputValue,
                        ::selectTagFilter,
                        inputFieldFocusRequester,
                        "Add Tag"
                    )

                    var isCreateTagButtonFocused by remember { mutableStateOf(false) }
                    Button(
                        content = { Text("+ Create Tag") },
                        onClick = {
                            openAddTagModal.value = true
                        },
                        colors = ButtonColors(
                            containerColor = if (isCreateTagButtonFocused) Color(3, 104, 3, 255) else Color(
                                23,
                                176,
                                71
                            ),
                            contentColor = Color.White,
                            disabledContainerColor = Color(23, 176, 71),
                            disabledContentColor = Color.White,
                        ),
                        modifier = Modifier.padding(start = 12.dp, top = 12.dp)
                            .pointerHoverIcon(PointerIcon.Hand)
                            .onFocusChanged { focusState -> isCreateTagButtonFocused = focusState.isFocused }
                            .lightBorderIfFocused(isCreateTagButtonFocused, 2.dp)
                    )
                }
                SelectedTags(selectedTags.value, ::unselectTag)

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    var isCreateOrUpdateQuoteButtonFocused by remember { mutableStateOf(false) }
                    Button(
                        onClick = {
                            val quote = Quote(-1, contentText.text, sourceText.text, tags = selectedTags.value.toList())
                            quoteEditorMode.performAction(quote)
                            onDismissRequest()
                        },
                        colors = ButtonColors(
                            containerColor = if (isCreateOrUpdateQuoteButtonFocused) Color(15, 81, 186) else Color(
                                52,
                                161,
                                235
                            ),
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.White
                        ),
                        enabled = contentText.text != "" && sourceText.text != "",
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Hand)
                            .onFocusChanged { focusState -> isCreateOrUpdateQuoteButtonFocused = focusState.isFocused }
                            .lightBorderIfFocused(isCreateOrUpdateQuoteButtonFocused, 2.dp)

                    ) {
                        Text(quoteEditorMode.getButtonText())
                    }

                    var isCloseButtonFocused by remember { mutableStateOf(false) }
                    Button(
                        onClick = { onDismissRequest() },
                        colors = ButtonColors(
                            containerColor = if (isCloseButtonFocused) Color(156, 3, 3, 255) else Color(
                                201,
                                9,
                                35,
                                255
                            ),
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.Gray
                        ),
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Hand)
                            .onFocusChanged { focusState -> isCloseButtonFocused = focusState.isFocused }
                            .lightBorderIfFocused(isCloseButtonFocused, 2.dp)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
    if (openAddTagModal.value) {
        TagEditorModal(TagEditorMode.AddMode(addTag), ::hideAddTagModal)
    }
}
