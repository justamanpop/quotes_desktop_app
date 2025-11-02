package ports.driving

import Tag

interface ForTags {
    fun getTags(): List<Tag>
    fun addTag(tag: Tag)
    fun updateTag(tagId: Int, newName: String)
//    fun deleteQuote(quoteId: Int)
}