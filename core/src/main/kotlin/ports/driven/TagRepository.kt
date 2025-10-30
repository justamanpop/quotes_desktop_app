package ports.driven

import Tag

interface TagRepository {
    fun getTags(): List<Tag>
//    fun addQuote(quote: Quote)
//    fun deleteQuote(quoteId: Int)
}