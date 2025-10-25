import ports.driven.QuoteRepository
import ports.driving.ForQuotes

class QuoteCore(private val quoteRepository: QuoteRepository): ForQuotes {
    override fun getQuotes(): List<Quote> {
        return quoteRepository.getQuotes()
    }
}