package ports.driving

import Quote

interface ForQuotes {
    fun getQuotes(): List<Quote>
    fun addQuote(quote: Quote)
    fun addQuotes(quotes: List<Quote>)
}