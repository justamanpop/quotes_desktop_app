import ports.driven.QuoteRepository
import ports.driven.TagRepository
import ports.driving.ForQuotes
import ports.driving.ForTags

class AppCore(private val quoteRepository: QuoteRepository, private val tagRepository: TagRepository): ForQuotes, ForTags {
    override fun getQuotes(): List<Quote> {
        return quoteRepository.getQuotes()
    }

    override fun addQuote(quote: Quote) {
        quoteRepository.addQuote(quote)
    }

    override fun addQuotes(quotes: List<Quote>) {
        quoteRepository.addQuotes(quotes)
    }

    override fun updateQuote(quote: Quote) {
        quoteRepository.updateQuote(quote)
    }

    override fun deleteQuote(quoteId: Int) {
        quoteRepository.deleteQuote(quoteId)
    }

    override fun getTags(): List<Tag> {
        return tagRepository.getTags()
    }

    override fun addTag(tag: Tag) {
        return tagRepository.addTag(tag)
    }

    override fun updateTag(tagId: Int, newName: String) {
        tagRepository.updateTag(tagId, newName)
    }
}