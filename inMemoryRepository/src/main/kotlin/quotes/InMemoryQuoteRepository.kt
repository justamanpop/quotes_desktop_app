package repository.quotes

import Quote
import ports.driven.QuoteRepository
import ports.driven.TagRepository

class InMemoryQuoteRepository(
    val tagRepository: TagRepository,
    initialQuotes: List<Quote> = emptyList()
) : QuoteRepository {
    override fun getQuotes(): List<Quote> {
        return fakeQuotes
    }

    override fun addQuote(quote: Quote) {
        val quoteToAdd = quote.copy(id = maxId + 1)
        fakeQuotes += quoteToAdd
        maxId += 1
    }

    override fun addQuotes(quotes: List<Quote>) {
        quotes.forEach { quote ->
            addQuote(quote)
        }
    }

    override fun updateQuote(quote: Quote) {
        fakeQuotes = fakeQuotes.map { q ->
            if (q.id == quote.id) {
                quote
            } else {
                q
            }
        }
    }

    override fun deleteQuote(quoteId: Int) {
        fakeQuotes = fakeQuotes.filterNot { q -> q.id == quoteId }
    }

    override fun importQuotes(quotes: List<Quote>, overwrite: Boolean) {
        val tags = quotes.flatMap { q -> q.tags }

        if (overwrite) {
            tagRepository.importTags(tags, overwrite = true)
            importQuotesWithOverride(quotes)
            return
        }

        tagRepository.importTags(tags, overwrite = false)
        val quotesWithUpdatedTags = getQuotesWithUpdatedMergedTags(quotes)
        quotesWithUpdatedTags.forEach { newQuote ->
            val foundQuote = fakeQuotes.find { q -> q.source == newQuote.source && q.content == newQuote.content }
            if (foundQuote == null) {
                fakeQuotes += newQuote.copy(id = maxId + 1)
                maxId += 1
            } else {
                fakeQuotes =
                    fakeQuotes.map { existingQuote -> if (existingQuote.id == newQuote.id) newQuote else existingQuote }
            }
        }
    }

    private fun importQuotesWithOverride(quotes: List<Quote>) {
        fakeQuotes = quotes
        maxId = fakeQuotes.maxBy { it.id }.id
    }

    private fun getQuotesWithUpdatedMergedTags(quotes: List<Quote>): List<Quote> {
        val tagsByName = tagRepository.getTags().associateBy { t -> t.name }
        return quotes.map { q ->
            val mergedTags = q.tags.map { t -> tagsByName[t.name]!! }
            q.copy(tags = mergedTags)
        }
    }

    private var fakeQuotes = initialQuotes
    private var maxId = fakeQuotes.size
}