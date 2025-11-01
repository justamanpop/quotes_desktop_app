package ports.driving

import Tag

interface ForTags {
    fun getTags(): List<Tag>
    fun addTag(tag: Tag)
//    fun deleteQuote(quoteId: Int)
}