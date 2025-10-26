import Quote
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import ports.driven.QuoteRepository

class SqlLiteQuoteRepository(dbName: String): QuoteRepository {
    val conn: SQLiteConnection

    init {
        conn = BundledSQLiteDriver().open(dbName)
        conn.execSQL(
            "CREATE TABLE IF NOT EXISTS quotes (id INTEGER PRIMARY KEY, content TEXT, source TEXT)"
        )
    }
    override fun getQuotes(): List<Quote> {
        val quotes = mutableListOf<Quote>()
        conn.prepare(
            "SELECT * FROM quotes"
        ).use {
            statement ->
            while (statement.step()) {
                println("in a step in get quotes")
                quotes.add(Quote(statement.getInt(0), statement.getText(1),statement.getText(2)))
            }
        }
        return quotes
    }

    override fun addQuote(quote: Quote) {
        conn.prepare("INSERT INTO quotes(id, content, source) VALUES(?, ?, ?)").use {
            statement ->
            statement.bindInt(1, quote.id)
            statement.bindText(2, quote.content)
            statement.bindText(3, quote.source)
            statement.step()
        }
    }

    override fun addQuotes(quotes: List<Quote>) {
       quotes.forEach { quote ->
           addQuote(quote)
       }
    }
}