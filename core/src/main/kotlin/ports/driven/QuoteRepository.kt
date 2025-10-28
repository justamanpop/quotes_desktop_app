package ports.driven

import Quote

interface QuoteRepository {
    fun getQuotes(): List<Quote>
    fun addQuote(quote: Quote)
    fun addQuotes(quotes: List<Quote>)
    fun deleteQuote(quoteId: Int)
}