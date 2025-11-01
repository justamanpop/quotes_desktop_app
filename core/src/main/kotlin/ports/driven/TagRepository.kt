package ports.driven

import Tag

interface TagRepository {
    fun getTags(): List<Tag>
    fun addTag(tag: Tag)
//    fun deleteQuote(quoteId: Int)
}