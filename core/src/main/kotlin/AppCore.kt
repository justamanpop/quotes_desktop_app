import kotlinx.serialization.json.Json
import ports.driven.QuoteRepository
import ports.driven.TagRepository
import ports.driving.ForSync
import ports.driving.ForQuotes
import ports.driving.ForTags

class AppCore(private val quoteRepository: QuoteRepository, private val tagRepository: TagRepository): ForQuotes, ForTags, ForSync {
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

    override fun updateTag(tag: Tag) {
        tagRepository.updateTag(tag)
    }

    override fun deleteTag(tagId: Int) {
        tagRepository.deleteTag(tagId)
    }

    override fun exportQuotesToJson(): String {
        val quotes = quoteRepository.getQuotes()
        return Json.encodeToString(quotes)
    }

    override fun importFromJson(jsonString: String, overwrite: Boolean) {
        val quotes: List<Quote> = Json.decodeFromString(jsonString)
        quoteRepository.importQuotes(quotes, overwrite)
    }
}