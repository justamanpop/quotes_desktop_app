package org.example.quotes.tagEditorModal

import Tag

sealed interface TagEditorMode {
    data class AddMode(val addTag: (Tag) -> Unit): TagEditorMode
    data class EditMode(val tag: Tag, val updateTag: (tagId: Int, newName: String) -> Unit): TagEditorMode
}

fun getInitialTagTextFieldValue(tagEditorMode: TagEditorMode): String {
    return when (tagEditorMode) {
        is TagEditorMode.AddMode -> {
            ""
        }
        is TagEditorMode.EditMode -> {
            tagEditorMode.tag.name
        }
    }
}