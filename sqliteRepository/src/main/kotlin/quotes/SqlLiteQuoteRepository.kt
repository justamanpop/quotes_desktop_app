package repository.quotes

import Quote
import Tag
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import ports.driven.QuoteRepository
import ports.driven.TagRepository

class SqlLiteQuoteRepository(val conn: SQLiteConnection, val tagRepository: TagRepository) : QuoteRepository {
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
        }


        val res = quotesMap.values.map { quote ->
            quote.copy(tags = tagsMap[quote.id] ?: emptyList())
        }
        return res
    }

    override fun addQuote(quote: Quote) {
        conn.execSQL("BEGIN TRANSACTION;")
        try {
            addQuoteQuery(quote)
            conn.execSQL("COMMIT;")
        } catch (e: Exception) {
            conn.execSQL("ROLLBACK;")
            throw e
        }
    }

    override fun addQuotes(quotes: List<Quote>) {
        quotes.forEach { quote ->
            addQuote(quote)
        }
    }

    override fun updateQuote(quote: Quote) {
        conn.prepare("select id from quotes WHERE id = ?").use { statement ->
            statement.bindInt(1, quote.id)
            if (!statement.step()) {
                throw IllegalArgumentException("Quote with given ID not found")
            }
        }

        conn.execSQL("BEGIN TRANSACTION;")
        try {
            conn.prepare("UPDATE quotes SET content = ?, source = ? WHERE id = ?").use { statement ->
                statement.bindText(1, quote.content)
                statement.bindText(2, quote.source)
                statement.bindInt(3, quote.id)
                statement.step()
            }


            conn.prepare("DELETE FROM quote_tag_mapping where quote_id = ?").use { statement ->
                statement.bindInt(1, quote.id)
                statement.step()
            }

            quote.tags.forEach { tag ->
                conn.prepare("INSERT INTO quote_tag_mapping(quote_id, tag_id) VALUES (?, ?)").use { statement ->
                    statement.bindInt(1, quote.id)
                    statement.bindInt(2, tag.id)
                    statement.step()
                }
            }

            conn.execSQL("COMMIT;")
        } catch (e: Exception) {
            conn.execSQL("ROLLBACK;")
            throw e
        }
    }

    override fun deleteQuote(quoteId: Int) {
        conn.prepare("select id from quotes WHERE id = ?").use { statement ->
            statement.bindInt(1, quoteId)
            if (!statement.step()) {
                throw IllegalArgumentException("Quote with given ID not found")
            }
        }

        conn.execSQL("BEGIN TRANSACTION;")
        try {
            conn.prepare("DELETE FROM quote_tag_mapping WHERE quote_id = ?").use { statement ->
                statement.bindInt(1, quoteId)
                statement.step()
            }

            conn.prepare("DELETE FROM quotes WHERE id = ?").use { statement ->
                statement.bindInt(1, quoteId)
                statement.step()
            }
            conn.execSQL("COMMIT;")
        } catch (e: Exception) {
            conn.execSQL("ROLLBACK;")
            throw e
        }
    }

    override fun importQuotes(quotesToImport: List<Quote>, overwrite: Boolean) {
        if (overwrite) {
            importWithOverwrite(quotesToImport)
            return
        }

        val existingTags = tagRepository.getTags()
        val importTags = quotesToImport.flatMap { q -> q.tags }

        conn.execSQL("BEGIN TRANSACTION;")

        importTags.distinctBy { it.name }.forEach { tag ->
            if (existingTags.find { et -> et.name == tag.name } == null) {
                tagRepository.addTag(tag)
            }
        }
        val newTags = tagRepository.getTags()
        val existingQuotes = getQuotes()

        try {
            quotesToImport.forEach { quoteToImport ->
                val existingQuoteMatch =
                    existingQuotes.find { existingQuote -> existingQuote.source == quoteToImport.source && existingQuote.content == quoteToImport.content }
                if (existingQuoteMatch == null) {
                    val quoteWithUpdatedTags =
                        quoteToImport.copy(tags = quoteToImport.tags.map { t -> newTags.find { it.name == t.name }!! })
                    addQuoteQuery(quoteWithUpdatedTags)
                } else {
                    val tagIdToMapToQuote = quoteToImport.tags
                        .asSequence()
                        .mapNotNull { t ->
                            newTags.find { it.name == t.name }
                        }
                        //if part of existing quote, no need to add quote tag mapping
                        .filter { t -> existingQuoteMatch.tags.find { it.name == t.name } == null }
                        .distinctBy { it.name }
                        .map { it.id }
                        .toList()
                    addQuoteTagMappingsQuery(existingQuoteMatch.id, tagIdToMapToQuote)
                }
            }
            conn.execSQL("COMMIT;")
        } catch (e: Exception) {
            conn.execSQL("ROLLBACK;")
            throw e
        }

    }

    private fun importWithOverwrite(quotes: List<Quote>) {
        conn.execSQL("BEGIN TRANSACTION;")
        try {
            clearDBQueries()
        } catch (e: Exception) {
            conn.execSQL("ROLLBACK;")
            throw e
        }

        try {
            quotes.flatMap { it.tags }.distinctBy { it.name }.forEach { tagRepository.addTag(it) }
            val newTags = tagRepository.getTags()

            quotes.forEach { quote ->
                val quoteWithUpdatedTags =
                    quote.copy(tags = quote.tags.map { t -> newTags.find { it.name == t.name }!! })
                addQuoteQuery(quoteWithUpdatedTags)
            }
            conn.execSQL("COMMIT;")
        } catch (e: Exception) {
            conn.execSQL("ROLLBACK;")
            throw e
        }
    }

    /**
     * Does not do transactions, that is to be handled by calling function
     */
    private fun clearDBQueries() {
        conn.prepare("DELETE FROM quote_tag_mapping").use { statement ->
            statement.step()
        }
        conn.prepare("DELETE FROM tags").use { statement ->
            statement.step()
        }
        conn.prepare("DELETE FROM quotes").use { statement ->
            statement.step()
        }
    }

    /**
     * Does not do transactions, that is to be handled by calling function
     */
    private fun addQuoteQuery(quote: Quote) {
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
    }

    private fun addQuoteTagMappingsQuery(quoteId: Int, tagIds: List<Int>) {
        tagIds.forEach { tagId ->
            conn.prepare("INSERT INTO quote_tag_mapping(quote_id, tag_id) VALUES(?, ?)").use { statement ->
                statement.bindInt(1, quoteId)
                statement.bindInt(2, tagId)
                statement.step()
            }

        }
    }
}