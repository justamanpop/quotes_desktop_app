package ports.driven

import Tag

interface TagRepository {
    fun getTags(): List<Tag>
    fun addTag(tag: Tag)
    fun updateTag(tagId: Int, newName: String)
}