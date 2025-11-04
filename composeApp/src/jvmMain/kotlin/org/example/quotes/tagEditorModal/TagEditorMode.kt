package org.example.quotes.tagEditorModal

import Tag

sealed interface TagEditorMode {
    data class AddMode(val addTag: (Tag) -> Unit): TagEditorMode
    data class EditMode(val tag: Tag, val updateTag: (tagId: Int, newName: String) -> Unit): TagEditorMode
}