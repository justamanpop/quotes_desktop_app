package ports.driven

import Tag

interface TagRepository {
    fun getTags(): List<Tag>
    fun addTag(tag: Tag)

    fun updateTag(tag: Tag)
    fun deleteTag(tagId: Int)
    fun importTags(tags: List<Tag>, overwrite: Boolean)
}