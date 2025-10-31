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
import moveFocusOnTab
import org.example.quotes.selectedTags.SelectedTags

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterQuotesModal(
    tags: List<Tag>,
    existingTagFilters: Set<Tag>,
    setTagFilters: (Set<Tag>) -> Unit,
    onDismissRequest: () -> Unit
) {
    val inputFieldFocusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        inputFieldFocusRequester.requestFocus()
    }

    val selectedTags = remember { mutableStateOf(existingTagFilters) }
    fun unselectTag(tag: Tag) {
        selectedTags.value = selectedTags.value.minus(tag)
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
            usePlatformDefaultWidth = false,
        ),
    ) {
        Card(modifier = Modifier.width(660.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(16.dp).fillMaxWidth().moveFocusOnTab()
            ) {
                Row {
                    SearchableDropdown(
                        tags.minus(selectedTags.value),
                        dropdownTextFieldValue,
                        ::setDropdownTextFieldState,
                        setDropdownInputValue,
                        ::selectTagFilter,
                        inputFieldFocusRequester,
                    )
                    Button(
                        content = { Text("+ Create Tag") },
                        onClick = {
                        },
                        colors = ButtonColors(
                            containerColor = Color(23, 176, 71),
                            contentColor = Color.White,
                            disabledContainerColor = Color(23, 176, 71),
                            disabledContentColor = Color.White,
                        ),
                        modifier = Modifier.padding(start = 24.dp)
                    )
                    SelectedTags(selectedTags.value, ::unselectTag, Modifier.padding(start = 12.dp))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        content = { Text("Apply Filter") }, colors = ButtonColors(
                            containerColor = Color(52, 161, 235),
                            contentColor = Color.White,
                            disabledContainerColor = Color(52, 161, 235),
                            disabledContentColor = Color.White,
                            ), onClick = {
                            setTagFilters(selectedTags.value)
                            onDismissRequest()
                        })
                    Button(content = { Text("Reset All") }, colors = ButtonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Red,
                        disabledContentColor = Color.White,
                    ), onClick = {
                        setTagFilters(setOf())
                        onDismissRequest()
                    })
                }
            }
        }
    }
}