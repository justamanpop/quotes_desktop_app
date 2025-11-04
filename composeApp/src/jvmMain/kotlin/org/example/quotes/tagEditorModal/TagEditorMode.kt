package org.example.quotes.tagEditorModal

import Tag

sealed interface TagEditorMode {
    val tag: Tag?
    fun performAction(tagToAddOrUpdate: Tag)
    data class AddMode(val addTag: (Tag) -> Unit) : TagEditorMode {
        override val tag: Tag? = null
        override fun performAction(tagToAddOrUpdate: Tag) {
            addTag(tagToAddOrUpdate)
        }
    }
    data class EditMode(override val tag: Tag, val updateTag: (tag: Tag) -> Unit) : TagEditorMode {
        override fun performAction(tagToAddOrUpdate: Tag) {
            updateTag(tagToAddOrUpdate.copy(id = tag.id))
        }
    }
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
