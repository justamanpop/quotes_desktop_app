package org.example.quotes.filterQuotesModal

import Tag
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.PopupProperties
import moveFocusOnTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterQuotesModal(tags: List<Tag>, onDismissRequest: () -> Unit) {
    val options = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5")
    var selectedOptionText by remember { mutableStateOf("") }

    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    /*val filterTagTerm = remember { mutableStateOf("") }

    val filteredTags = tags.filter { t -> t.name.lowercase().contains(filterTagTerm.value, ignoreCase = true) }*/

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
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = setExpanded,
                ) {
                    TextField(
                        // The `menuAnchor` modifier must be passed to the text field for correctness.
                        modifier = Modifier.menuAnchor(),
                        value = selectedOptionText,
                        onValueChange = {
                            selectedOptionText = it
                            setExpanded(true)
                        },
                        label = { Text("Label") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    )
                    // filter options based on text field value
                    val filteringOptions = tags.filter { it.name.contains(selectedOptionText, ignoreCase = true) }
                    if (filteringOptions.isNotEmpty()) {
                        DropdownMenu(
                            modifier = Modifier
                                .background(Color.White)
                                .exposedDropdownSize(true),
                            properties = PopupProperties(focusable = false),
                            expanded = expanded,
                            onDismissRequest = { setExpanded(false) },
                        ) {
                            filteringOptions.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption.name) },
                                    onClick = {
                                        selectedOptionText = selectionOption.name
                                        setExpanded(false)
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }
                    }
                }
                /*ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = setExpanded) {
                    OutlinedTextField(
                        value = filterTagTerm.value,
                        onValueChange = { v ->
                            filterTagTerm.value = v
                        },
                        label = { Text("Tag") },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true)
                    )

                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = {
                        setExpanded(false)
                    }) {
                        filteredTags.forEach { tag ->
                            DropdownMenuItem({ Text(tag.name) }, onClick = {
                                filterTagTerm.value = tag.name
                                setExpanded(false)
                            })
                        }
                    }
                }*/



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