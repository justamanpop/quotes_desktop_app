package org.example.quotes.filterQuotesModal

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.window.PopupProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SearchableDropdown(options: List<T>, optionDisplayFunction: (T) -> String, onSelect: (T) -> Unit) {
    val (showDropdown, setShowDropdown) = remember { mutableStateOf(false) }

    val filterTagTerm = remember { mutableStateOf("") }
    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = filterTagTerm.value,
                selection = TextRange(filterTagTerm.value.length)
            )
        )
    }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    ExposedDropdownMenuBox(
                            expanded = showDropdown,
                            onExpandedChange = setShowDropdown,
                        ) {
                            TextField(
                                // The `menuAnchor` modifier must be passed to the text field for correctness.
                                modifier = Modifier.menuAnchor().focusRequester(focusRequester),
                                value = textFieldValueState,
                                onValueChange = {
                                    filterTagTerm.value = it.text
                                    textFieldValueState = it
                                    setShowDropdown(true)
                                },
                                label = { Text("Tag") },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            )
                            // filter options based on text field value
                            val filteredTags = options.filter { optionDisplayFunction(it).contains(filterTagTerm.value, ignoreCase = true) }
                            if (filteredTags.isNotEmpty()) {
                                DropdownMenu(
                                    modifier = Modifier
                                        .background(Color.White)
                                        .exposedDropdownSize(true),
                                    properties = PopupProperties(focusable = false),
                                    expanded = showDropdown,
                                    onDismissRequest = { setShowDropdown(false) },
                                ) {
                                    filteredTags.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(optionDisplayFunction(option)) },
                                            onClick = {
                                                val optionText = optionDisplayFunction(option)
                                                filterTagTerm.value = optionText
                                                textFieldValueState = TextFieldValue(optionText, TextRange(optionText.length))
                                                onSelect(option)
                                                setShowDropdown(false)
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                        )
                                    }
                                }
                            }
                        }
}