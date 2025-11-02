package org.example.quotes.filterQuotesModal

import Tag
import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.PopupProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagSearchableDropdown(
    options: List<Tag>,
    textFieldValueState: TextFieldValue,
    setTextFieldValue: (TextFieldValue) -> Unit,
    setDropdownValue: (String) -> Unit,
    onSelect: (Tag) -> Unit,
    inputFieldFocusRequester: FocusRequester,
) {
    val (showDropdown, setShowDropdown) = remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = showDropdown,
        onExpandedChange = setShowDropdown,
    ) {
        TextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier.menuAnchor().focusRequester(inputFieldFocusRequester),
            value = textFieldValueState,
            onValueChange = {
                setDropdownValue(it.text)
                setTextFieldValue(it)
                setShowDropdown(true)
            },
            label = { Text("Tags") },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        // filter options based on text field value
        val filteredTags = options.filter { it.name.contains(textFieldValueState.text, ignoreCase = true) }
        if (filteredTags.isNotEmpty()) {
            DropdownMenu(
                modifier = Modifier
                    .background(Color.White)
                    .exposedDropdownSize(true),
                properties = PopupProperties(focusable = false),
                expanded = showDropdown,
                onDismissRequest = { setShowDropdown(false) },
            ) {
                filteredTags.forEach { tag ->
                    DropdownMenuItem(
                        text = { Text(tag.name) },
                        onClick = {
                            val name = tag.name
                            setDropdownValue(name)

                            setTextFieldValue(TextFieldValue(name, TextRange(name.length)))

                            onSelect(tag)
                            setShowDropdown(false)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
    }
}