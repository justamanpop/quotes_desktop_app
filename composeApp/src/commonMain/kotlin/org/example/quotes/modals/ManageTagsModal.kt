package org.example.quotes.modals

import Tag
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.example.quotes.modals.tagEditorModal.TagEditorModal
import org.example.quotes.shared.DeleteConfirmationModal
import org.example.quotes.shared.TagSearchableDropdown
import org.example.quotes.modals.tagEditorModal.TagEditorMode
import org.example.quotes.shared.lightBorderIfFocused
import org.example.quotes.shared.moveFocusOnTab

@Composable
fun ManageTagsModal(
    tags: List<Tag>,
    addTag: (Tag) -> Unit,
    updateTag: (tag: Tag) -> Unit,
    deleteTag: (tagId: Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        ),
    ) {

        val inputFieldFocusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            inputFieldFocusRequester.requestFocus()
        }

        val openAddTagModal = remember { mutableStateOf(false) }
        fun hideAddTagModal() {
            openAddTagModal.value = false
            inputFieldFocusRequester.requestFocus()
        }

        val openUpdateTagModal = remember { mutableStateOf(false) }
        fun hideUpdateTagModal() {
            openUpdateTagModal.value = false
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
        }

        fun updateTagInModal(tag: Tag) {
            updateTag(tag)
            setDropdownInputValue("")
            setDropdownTextFieldState(TextFieldValue())
        }

        fun deleteTagInModal(tagId: Int) {
            deleteTag(tagId)
            setDropdownInputValue("")
            setDropdownTextFieldState(TextFieldValue())
        }


        val openDeleteTagConfirmationModal = remember { mutableStateOf(false) }
        fun hideDeleteTagConfirmationModal() {
            openDeleteTagConfirmationModal.value = false
        }

        Card(modifier = Modifier.width(660.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(16.dp).fillMaxWidth().moveFocusOnTab()
            ) {
                var isCreateTagButtonFocused by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        openAddTagModal.value = true
                    },
                    colors = ButtonColors(
                        containerColor = if (isCreateTagButtonFocused) Color(3, 104, 3, 255) else Color(23, 176, 71),
                        contentColor = Color.White,
                        disabledContainerColor = Color(23, 176, 71),
                        disabledContentColor = Color.White,
                    ),
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand)
                        .onFocusChanged { focusState -> isCreateTagButtonFocused = focusState.isFocused }
                        .lightBorderIfFocused(isCreateTagButtonFocused, 2.dp)
                ) {
                    Text("+ Create Tag")
                }

                HorizontalDivider()
                TagSearchableDropdown(
                    tags,
                    dropdownTextFieldValue,
                    ::setDropdownTextFieldState,
                    setDropdownInputValue,
                    ::selectTag,
                    inputFieldFocusRequester,
                    "Search Tags"
                )
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    var isEditTagButtonFocused by remember { mutableStateOf(false) }
                    Button(
                        onClick = {
                            openUpdateTagModal.value = true
                        },
                        colors = ButtonColors(
                            containerColor = if (isEditTagButtonFocused) Color(15, 81, 186) else Color(52, 161, 235),
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.White
                        ),
                        enabled = selectedTag != null && (tags.find { t -> t.name == dropdownInputValue } != null),
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                            .pointerHoverIcon(PointerIcon.Hand)
                            .onFocusChanged { focusState -> isEditTagButtonFocused = focusState.isFocused }
                            .lightBorderIfFocused(isEditTagButtonFocused, 2.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "edit")
                        Text(" Edit", color = Color.White, fontSize = 16.sp)
                    }

                    var isDeleteTagButtonFocused by remember { mutableStateOf(false) }
                    Button(
                        onClick = {
                            openDeleteTagConfirmationModal.value = true
                        },
                        colors = ButtonColors(
                            contentColor = Color.White,
                            containerColor = if (isDeleteTagButtonFocused) Color(156, 3, 3, 255) else Color(
                                201,
                                9,
                                35,
                                255
                            ),
                            disabledContentColor = Color.White,
                            disabledContainerColor = Color.Gray
                        ),
                        enabled = selectedTag != null && (tags.find { t -> t.name == dropdownInputValue } != null),
                        modifier = Modifier.padding(top = 16.dp)
                            .pointerHoverIcon(PointerIcon.Hand)
                            .onFocusChanged { focusState -> isDeleteTagButtonFocused = focusState.isFocused }
                            .lightBorderIfFocused(isDeleteTagButtonFocused, 2.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "delete")
                        Text(" Delete", color = Color.White, fontSize = 16.sp)
                    }
                }
            }
        }

        if (openAddTagModal.value) {
            TagEditorModal(TagEditorMode.AddMode(addTag), ::hideAddTagModal)
        }
        if (openUpdateTagModal.value && selectedTag != null) {
            TagEditorModal(TagEditorMode.EditMode(selectedTag, ::updateTagInModal), ::hideUpdateTagModal)
        }

        if (openDeleteTagConfirmationModal.value) {
            DeleteConfirmationModal(selectedTag?.id, ::deleteTagInModal, "tag", ::hideDeleteTagConfirmationModal)
        }
    }
}