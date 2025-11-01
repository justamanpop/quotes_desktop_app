package repository.quotes

import Quote
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import ports.driven.QuoteRepository

class SqlLiteQuoteRepository(dbName: String) : QuoteRepository {
    val conn: SQLiteConnection = BundledSQLiteDriver().open(dbName)

    override fun getQuotes(): List<Quote> {
        val quotes = mutableListOf<Quote>()
        conn.prepare(
            "SELECT * FROM quotes "
        ).use { statement ->
            while (statement.step()) {
                quotes.add(Quote(statement.getInt(0), statement.getText(1), statement.getText(2)))
            }
        }
        return quotes
    }

    override fun addQuote(quote: Quote) {
        conn.execSQL("BEGIN TRANSACTION;")

        try {
            conn.prepare("INSERT INTO quotes(content, source) VALUES(?, ?)").use { statement ->
                statement.bindText(1, quote.content)
                statement.bindText(2, quote.source)
                statement.step()
            }

            var insertedQuoteId = -1
            conn.prepare("SELECT last_insert_rowid();").use { statement ->
                while (statement.step()) {
                    insertedQuoteId = statement.getInt(0)
                }
            }

            quote.tags.forEach { tag ->
                conn.prepare("INSERT INTO quote_tag_mapping(quote_id, tag_id) VALUES(?, ?)").use { statement ->
                    statement.bindInt(1, insertedQuoteId)
                    statement.bindInt(2, tag.id)
                    statement.step()
                }
            }
            conn.execSQL("COMMIT;")
        } catch (e: Exception) {
            conn.execSQL("ROLLBACK;")
            throw e;
        }
    }

    override fun addQuotes(quotes: List<Quote>) {
        quotes.forEach { quote ->
            addQuote(quote)
        }
    }

    override fun deleteQuote(quoteId: Int) {
        conn.prepare("DELETE FROM quotes WHERE id = ?").use { statement ->
            statement.bindInt(1, quoteId)
            statement.step()
        }
    }
}