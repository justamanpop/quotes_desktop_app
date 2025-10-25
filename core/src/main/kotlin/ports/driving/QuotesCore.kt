package ports.driving

import Quote

interface ForQuotes {
    fun getQuotes(): List<Quote>
}