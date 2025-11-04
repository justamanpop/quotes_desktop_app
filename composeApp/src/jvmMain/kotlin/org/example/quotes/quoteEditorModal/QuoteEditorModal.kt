package org.example.quotes.quoteEditorModal

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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import moveFocusOnTab
import org.example.quotes.filterQuotesModal.TagSearchableDropdown
import org.example.quotes.selectedTags.SelectedTags

@Composable
fun QuoteEditorModal(quoteEditorMode: QuoteEditorMode, tags: List<Tag>, onDismissRequest: () -> Unit) {
    var contentText by remember {
        val content = quoteEditorMode.quote?.content ?: ""
        mutableStateOf(TextFieldValue(content, TextRange(content.length)))
    }
    var sourceText by remember {
        val source = quoteEditorMode.quote?.source ?: ""
        mutableStateOf(TextFieldValue(source, TextRange(source.length)))
    }

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
                    onValueChange = { contentText = it },
                    label = { Text("Content") },
                    modifier = Modifier.height(220.dp).fillMaxWidth().focusRequester(focusRequester).moveFocusOnTab()
                )
                OutlinedTextField(
                    value = sourceText,
                    onValueChange = { sourceText = it },
                    label = { Text("Source") },
                    modifier = Modifier.fillMaxWidth()
                )

                TagSearchableDropdown(
                    tags.minus(selectedTags.value),
                    dropdownTextFieldValue,
                    ::setDropdownTextFieldState,
                    setDropdownInputValue,
                    ::selectTagFilter,
                    inputFieldFocusRequester,
                )
                SelectedTags(selectedTags.value, ::unselectTag)

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(onClick = {
                        val quote = Quote(-1, contentText.text, sourceText.text, tags = selectedTags.value.toList())
                        quoteEditorMode.performAction(quote)
                        onDismissRequest()
                    }) {
                        val buttonText = when (quoteEditorMode) {
                            is QuoteEditorMode.AddMode -> "Create Quote"
                            is QuoteEditorMode.EditMode -> "Update Quote"
                        }
                        Text(buttonText)
                    }
                    Button(content = { Text("Close") }, onClick = { onDismissRequest() })
                }
            }
        }
    }
}
