package repository.quotes

import Quote
import Tag
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import ports.driven.QuoteRepository

class SqlLiteQuoteRepository(val conn: SQLiteConnection) : QuoteRepository {
    override fun getQuotes(): List<Quote> {
        val quotesMap = mutableMapOf<Int, Quote>()
        val tagsMap = mutableMapOf<Int, MutableList<Tag>>()
        conn.prepare(
            "SELECT * FROM quotes LEFT JOIN quote_tag_mapping ON quotes.id = quote_tag_mapping.quote_id LEFT JOIN tags ON quote_tag_mapping.tag_id = tags.id"
        ).use { statement ->
            while (statement.step()) {
                val quoteId = statement.getInt(0)
                val quoteContent = statement.getText(1)
                val quoteSource = statement.getText(2)

                quotesMap.getOrPut(quoteId) { Quote(quoteId, quoteContent, quoteSource) }

                val tagId = statement.getInt(5)
                val tagName = statement.getText(6)
                val tag = Tag(tagId, tagName)

                if (tagId != 0) {
                    tagsMap.getOrPut(quoteId) { mutableListOf() }.add(tag)
                }
            }
            statement.close()
        }


        val res = quotesMap.values.map { quote ->
            quote.copy(tags = tagsMap[quote.id] ?: emptyList())
        }
        return res
    }

    override fun addQuote(quote: Quote) {
        conn.execSQL("BEGIN TRANSACTION;")
        try {
            conn.prepare("INSERT INTO quotes(content, source) VALUES(?, ?)").use { statement ->
                statement.bindText(1, quote.content)
                statement.bindText(2, quote.source)
                statement.step()
                statement.close()
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
                    statement.close()
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

    override fun updateQuote(quote: Quote) {
        conn.execSQL("BEGIN TRANSACTION;")
        try {
            conn.prepare("UPDATE quotes SET content = ?, source = ? WHERE id = ?").use { statement ->
                statement.bindText(1, quote.content)
                statement.bindText(2, quote.source)
                statement.bindInt(3, quote.id)
                statement.step()
                statement.close()
            }


            conn.prepare("DELETE FROM quote_tag_mapping where quote_id = ?").use { statement ->
                statement.bindInt(1, quote.id)
                statement.step()
                statement.close()
            }

            quote.tags.forEach { tag ->
                conn.prepare("INSERT INTO quote_tag_mapping(quote_id, tag_id) VALUES (?, ?)").use { statement ->
                    statement.bindInt(1, quote.id)
                    statement.bindInt(2, tag.id)
                    statement.step()
                    statement.close()
                }
            }

            conn.execSQL("COMMIT;")
        } catch (e: Exception) {
            conn.execSQL("ROLLBACK;")
            throw e;
        }
    }

    override fun deleteQuote(quoteId: Int) {
        conn.execSQL("BEGIN TRANSACTION;")
        try {
            conn.prepare("DELETE FROM quote_tag_mapping WHERE quote_id = ?").use { statement ->
                statement.bindInt(1, quoteId)
                statement.step()
                statement.close()
            }

            conn.prepare("DELETE FROM quotes WHERE id = ?").use { statement ->
                statement.bindInt(1, quoteId)
                statement.step()
                statement.close()
            }
            conn.execSQL("COMMIT;")
        } catch (e: Exception) {
            conn.execSQL("ROLLBACK;")
            throw e;
        }
    }
}