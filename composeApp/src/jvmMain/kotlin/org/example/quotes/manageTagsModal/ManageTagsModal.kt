package org.example.quotes.manageTagsModal

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
import moveFocusOnTab
import org.example.quotes.addTagModal.AddTagModal
import org.example.quotes.filterQuotesModal.TagSearchableDropdown
import kotlin.collections.plus

@Composable
fun ManageTagsModal(tags: List<Tag>, addTag: (Tag) -> Unit, deleteTag: (Tag) -> Unit, onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
    ) {
        val inputFieldFocusRequester = remember { FocusRequester() }

        val openAddTagModal = remember { mutableStateOf(false) }
        fun hideAddTagModal() {
            openAddTagModal.value = false
            inputFieldFocusRequester.requestFocus()
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

        val (selectedTag, setSelectedTag) = remember { mutableStateOf<Tag?>(null) }
        fun selectTag(tag: Tag) {
            setSelectedTag(tag)
            setDropdownInputValue("")
            dropdownTextFieldValue = TextFieldValue("")
        }

        Card(modifier = Modifier.width(660.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(16.dp).fillMaxWidth().moveFocusOnTab()
            ) {
                Button(
                    content = { Text("+ Create Tag") },
                    onClick = {
                        openAddTagModal.value = true
                    },
                    colors = ButtonColors(
                        containerColor = Color(23, 176, 71),
                        contentColor = Color.White,
                        disabledContainerColor = Color(23, 176, 71),
                        disabledContentColor = Color.White,
                    ),
                    modifier = Modifier.padding(start = 24.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    TagSearchableDropdown(tags, dropdownTextFieldValue, ::setDropdownTextFieldState, setDropdownInputValue, ::selectTag, inputFieldFocusRequester)
                    Button(content = { Text("Edit") }, onClick = {
//                        onDismissRequest()
                    })
                    Button(content = { Text("Delete") }, onClick = {
//                        onDismissRequest()
                    })
                }
            }
        }

        if (openAddTagModal.value) {
            AddTagModal(addTag, ::hideAddTagModal)
        }
    }
}