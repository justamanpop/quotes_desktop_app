package org.example.quotes.tagEditorModal

import Tag

sealed interface TagEditorMode {
    data class AddMode(val addTag: (Tag) -> Unit) : TagEditorMode
    data class EditMode(val tag: Tag, val updateTag: (tag: Tag) -> Unit) : TagEditorMode
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

fun getOnAddOrEditButtonClickFunc(tagEditorMode: TagEditorMode, tagName: String): () -> Unit {
    return when (tagEditorMode) {
        is TagEditorMode.AddMode -> {
            {
                tagEditorMode.addTag(Tag(-1, tagName))
            }
        }
        is TagEditorMode.EditMode -> {
            {
                tagEditorMode.updateTag(Tag(tagEditorMode.tag.id, tagName))
            }
        }
    }
}

fun getOnAddOrEditButtonText(tagEditorMode: TagEditorMode): String {
    return when (tagEditorMode) {
        is TagEditorMode.AddMode -> {
            "Create Tag"
        }
        is TagEditorMode.EditMode -> {
            "Update Tag"
        }
    }
}
