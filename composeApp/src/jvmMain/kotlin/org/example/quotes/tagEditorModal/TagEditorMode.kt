package org.example.quotes.tagEditorModal

import Quote
import Tag

sealed interface TagEditorMode {
    val tag: Tag?
    fun performAction(tagToAddOrUpdate: Tag)
    fun getInitialTagName(): String
    fun getButtonText(): String
    data class AddMode(val addTag: (Tag) -> Unit) : TagEditorMode {
        override val tag: Tag? = null
        override fun performAction(tagToAddOrUpdate: Tag) {
            addTag(tagToAddOrUpdate)
        }
        override fun getInitialTagName(): String {
            return ""
        }
        override fun getButtonText(): String {
            return "Create Tag"
        }
    }
    data class EditMode(override val tag: Tag, val updateTag: (tag: Tag) -> Unit) : TagEditorMode {
        override fun performAction(tagToAddOrUpdate: Tag) {
            updateTag(tagToAddOrUpdate.copy(id = tag.id))
        }
        override fun getInitialTagName(): String {
            return tag.name
        }
        override fun getButtonText(): String {
            return "Update Tag"
        }
    }
}