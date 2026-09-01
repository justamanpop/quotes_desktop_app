package repository.quotes

import Quote
import Tag
import ports.driven.QuoteRepository
import ports.driven.TagRepository
import kotlin.collections.forEach

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
        var found = false
        fakeQuotes = fakeQuotes.map { q ->
            if (q.id == quote.id) {
                found = true
                quote
            } else {
                q
            }
        }
        if (!found) {
            throw IllegalArgumentException("Quote with given ID not found")
        }
    }

    override fun deleteQuote(quoteId: Int) {
        val quoteToDelete = fakeQuotes.find { q -> q.id == quoteId }
        if (quoteToDelete == null) {
            throw IllegalArgumentException("Quote with given ID not found")
        }
        fakeQuotes = fakeQuotes.filterNot { q -> q.id == quoteId }
    }

    override fun importQuotes(quotesToImport: List<Quote>, overwrite: Boolean) {
        val quotesToImport = quotesToImport.distinctBy { q -> q.source + q.content }
        val importedTags = quotesToImport.flatMap { q -> q.tags }

        if (overwrite) {
            tagRepository.importTags(importedTags, overwrite = true)
            importQuotesWithOverride(quotesToImport)
            return
        }

        tagRepository.importTags(importedTags, overwrite = false)

        val currentQuotes = getQuotes()
        val currentTags = tagRepository.getTags()

        val quotesToImportWithUpdatedTags = quotesToImport.map {
            quoteToImport ->
            val updatedTags = mutableListOf<Tag>()
            quoteToImport.tags.forEach {
                val foundTag = currentTags.find { existingTag -> existingTag.name == it.name }

                //since import tag was called earlier, this should always be a true condition
                if(foundTag != null) {
                    updatedTags.add(foundTag)
                }
            }
            quoteToImport.copy(tags = updatedTags)
        }

        val mergedQuotes = mutableListOf<Quote>()

        //quotes that are currently present that are not in imported quotes are part of merged list
        currentQuotes.forEach { quote ->
            val foundQuote =
                quotesToImportWithUpdatedTags.find { quoteToImport -> quoteToImport.source == quote.source && quoteToImport.content == quote.content }
            if(foundQuote == null) {
               mergedQuotes.add(quote)
            }
        }

        //adds completely new quotes, as well as handles quotes to import that already currently exist
        quotesToImportWithUpdatedTags.forEach { quoteToImport ->
            val foundQuote =
                currentQuotes.find { existingQuote -> existingQuote.source == quoteToImport.source && existingQuote.content == quoteToImport.content }
            if (foundQuote == null) {
                maxId += 1
                mergedQuotes.add(quoteToImport.copy(id = maxId))
            } else {
                val mergedTags = (foundQuote.tags + quoteToImport.tags).distinct()
                mergedQuotes.add(foundQuote.copy(tags = mergedTags))
            }
        }
        fakeQuotes = mergedQuotes
    }

    private fun importQuotesWithOverride(quotes: List<Quote>) {
        fakeQuotes = listOf()
        maxId = 0
        quotes.forEach { quoteToInsert ->
            maxId += 1
            fakeQuotes += quoteToInsert.copy(id = maxId)
        }
    }

    private fun getQuotesWithUpdatedMergedTags(quotes: List<Quote>, updatedTags: List<Tag>): List<Quote> {
        val tagsByName = updatedTags.associateBy { t -> t.name }
        return quotes.map { q ->
            val mergedTags = q.tags.map { t -> tagsByName[t.name]!! }
            q.copy(tags = mergedTags)
        }
    }

    private var fakeQuotes = initialQuotes
    private var maxId = fakeQuotes.size
}