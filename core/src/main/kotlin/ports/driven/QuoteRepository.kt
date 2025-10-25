package ports.driven

import Quote

interface QuoteRepository {
    fun getQuotes(): List<Quote>
}