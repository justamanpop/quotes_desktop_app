package repository.quotes

import Quote
import Tag
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import repository.initializeDb
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.tags.SqlLiteTagRepository
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SqlLiteQuoteRepositoryTest {
    private lateinit var quoteRepository: SqlLiteQuoteRepository
    private lateinit var tagRepository: SqlLiteTagRepository

    private val seedTags = listOf(Tag(1, "Tag1"), Tag(2, "Tag2"), Tag(3, "Tag3"))
    private val seedQuotes = listOf(
        Quote(id = 1, source = "s1", content = "c1", tags = listOf(seedTags[0])),
        Quote(id = 2, source = "s2", content = "c2", tags = listOf(seedTags[1])),
        Quote(id = 3, source = "s3", content = "c3", tags = seedTags)
    )

    @BeforeEach
    fun setup() {
        val conn = BundledSQLiteDriver().open(":memory:")
        initializeDb(conn)
        tagRepository = SqlLiteTagRepository(conn)
        quoteRepository = SqlLiteQuoteRepository(conn, tagRepository)
        seedTags.forEach { tagRepository.addTag(it) }
        quoteRepository.addQuotes(seedQuotes)
    }

    @Test
    fun `get quotes returns seeded values from fresh repositories with no changes`() {
        //act
        val quotes = quoteRepository.getQuotes()

        //assert
        assertEquals(seedQuotes.size, quotes.size)

        assertEquals(quotes[0], seedQuotes[0])
        assertEquals(quotes[1], seedQuotes[1])
        assertEquals(quotes[2], seedQuotes[2])
    }

    @Test
    fun `after adding a quote, getting quotes returns existing quotes + added quote`() {
        //arrange
        val quoteToAdd = Quote(4, "c4", "s4", listOf())

        //act
        quoteRepository.addQuote(quoteToAdd)
        val quotes = quoteRepository.getQuotes()

        //assert
        assertEquals(seedQuotes.size + 1, quotes.size)
        assertContains(quotes, quoteToAdd)
    }

    @Test
    fun `updating a quote modifies particular quote and does not touch other quotes`() {
        //arrange
        val updatedQuote1 = Quote(seedQuotes[0].id, "cUpdated", "sUpdated", listOf(seedTags[0]))

        //act
        quoteRepository.updateQuote(updatedQuote1)
        val quotes = quoteRepository.getQuotes()

        //assert
        assertEquals(seedQuotes.size, quotes.size)
        assertContains(quotes, updatedQuote1)
        assertContains(quotes, seedQuotes[1])
    }

    @Test
    fun `updating a quote that does not exist throws an exception`() {
        //arrange
        val updatedQuote1 = Quote(99, "cUpdated", "sUpdated", listOf(seedTags[0]))

        //act, assert
        val exception = assertThrows<IllegalArgumentException> {
            quoteRepository.updateQuote(updatedQuote1)
        }
        assertEquals("Quote with given ID not found", exception.message)
    }

    @Test
    fun `deleting a quote deletes that particular quote and does not touch other quotes`() {
        //act
        quoteRepository.deleteQuote(seedQuotes[0].id)
        val quotes = quoteRepository.getQuotes()

        //assert
        assertEquals(seedQuotes.size - 1, quotes.size)
        assertFalse(quotes.contains(seedQuotes[0]))
        assertContains(quotes, seedQuotes[1])
        assertContains(quotes, seedQuotes[2])
    }

    @Test
    fun `deleting a quote that does not exist throws an exception`() {
        //act, assert
        val exception = assertThrows<IllegalArgumentException> {
            quoteRepository.deleteQuote(99)
        }
        assertEquals("Quote with given ID not found", exception.message)
    }

    @Test
    fun `import quotes with overwrite completely replaces existing quotes and tags`() {
        //arrange
        val tagsToImport = listOf(Tag(1, "Tag1"), Tag(12, "Tag2"), Tag(3, "TagUber"))
        val quotesToImport = listOf(
            Quote(1, "c1New", "s1New"),
            Quote(22, "c2New", "s2New", listOf(tagsToImport[0], tagsToImport[1])),
            Quote(3, "c3New", "s3New", tags = listOf(tagsToImport[1], tagsToImport[2]))
        )

        //act
        quoteRepository.importQuotes(quotesToImport, true)
        val tags = tagRepository.getTags()
        val quotes = quoteRepository.getQuotes()

        //assert
        assertEquals(tagsToImport.size, tags.size)
        assertEquals(quotesToImport.size, quotes.size)

        tagsToImport.forEach { tagToImport ->
            assertTrue(tags.any { t -> t.name == tagToImport.name })
        }
        assertFalse(tags.any { t -> t.name == seedTags[2].name })

        quotesToImport.forEach { quoteToImport ->
            assertTrue(quotes.any { q -> q.source == quoteToImport.source && q.content == quoteToImport.content && q.tags.map { it.name } == quoteToImport.tags.map { it.name } })
        }
        seedQuotes.forEach { seedQuote ->
            assertFalse(quotes.any { q -> q.source == seedQuote.source && q.content == seedQuote.content })
        }
    }

    @Test
    fun `import quotes without overwrite does not create new tag if a tag with same name exists, and keeps old tag id`() {
        //arrange
        val tagsToImport = listOf(Tag(9, seedTags[0].name), Tag(10, seedTags[1].name))
        val newQuotes = listOf(Quote(20, "cnew", "snew", tagsToImport))

        //act
        quoteRepository.importQuotes(newQuotes, false)

        //assert
        val tags = tagRepository.getTags()
        assertEquals(seedTags.size, tags.size)
        assertTrue(tags.any { t -> t.name == seedTags[0].name && t.id == seedTags[0].id })
        assertTrue(tags.any { t -> t.name == seedTags[1].name && t.id == seedTags[1].id })
    }

    @Test
    fun `import quotes without overwrite creates new tag if a tag with same name does not exist, gives it next id in sequence`() {
        //arrange
        val tagsToImport = listOf(Tag(9, "TagNew1"), Tag(10, "TagNew2"))
        val newQuotes = listOf(Quote(20, "cnew", "snew", tagsToImport))

        val currMaxTagId = tagRepository.getTags().maxBy { it.id }.id

        //act
        quoteRepository.importQuotes(newQuotes, false)

        //assert
        val tags = tagRepository.getTags()
        assertEquals(seedTags.size + 2, tags.size)
        assertTrue(tags.any { t -> t.name == "TagNew1" && t.id == currMaxTagId + 1 })
        assertTrue(tags.any { t -> t.name == "TagNew2" && t.id == currMaxTagId + 2 })
    }

    @Test
    fun `import quotes without overwrite creates new quote if a quote with same source and content does not exist, gives it next id in sequence`() {
        //arrange
        val tagsToImport = listOf(Tag(9, "TagNew1"), Tag(10, "TagNew2"))
        val newQuotes = listOf(Quote(20, "cnew", "snew", tagsToImport), Quote(22, "cnew2", "snew2", tagsToImport))

        val currMaxQuoteId = quoteRepository.getQuotes().size

        //act
        quoteRepository.importQuotes(newQuotes, false)

        //assert
        val quotes = quoteRepository.getQuotes()
        assertEquals(seedQuotes.size + 2, quotes.size)
        assertTrue(quotes.any { q -> q.source == "snew" && q.content == "cnew" && q.id == currMaxQuoteId + 1 })
        assertTrue(quotes.any { q -> q.source == "snew2" && q.content == "cnew2" && q.id == currMaxQuoteId + 2 })
    }

    @Test
    fun `import quotes without overwrite does not create new quote if a quote with same source and content exists, keeping old id`() {
        //arrange
        val newQuotes = listOf(
            Quote(20, seedQuotes[0].content, seedQuotes[0].source, listOf()),
            Quote(22, seedQuotes[1].content, seedQuotes[1].source, listOf())
        )

        //act
        quoteRepository.importQuotes(newQuotes, false)

        //assert
        val quotes = quoteRepository.getQuotes()
        assertEquals(seedQuotes.size, quotes.size)
        assertTrue(quotes.any { q -> q.source == newQuotes[0].source && q.content == newQuotes[0].content && q.id != newQuotes[0].id })
        assertTrue(quotes.any { q -> q.source == newQuotes[1].source && q.content == newQuotes[1].content && q.id != newQuotes[1].id })
    }

    @Test
    fun `import quotes without overwrite does not create new quote if a quote with same source and content exists, but keeps tags from both imported as well as existing quote`() {
        //arrange
        val tagsToImport = listOf(Tag(9, "TagNew1"), Tag(10, "TagNew2"))
        val newQuotes = listOf(
            Quote(20, seedQuotes[0].content, seedQuotes[0].source, tagsToImport),
            Quote(22, seedQuotes[1].content, seedQuotes[1].source, tagsToImport)
        )

        //act
        quoteRepository.importQuotes(newQuotes, false)

        //assert
        val quotes = quoteRepository.getQuotes()
        assertEquals(seedQuotes.size, quotes.size)

        val newQuote1 = quotes.find { q -> q.source == newQuotes[0].source && q.content == newQuotes[0].content }
        val newQuote2 = quotes.find { q -> q.source == newQuotes[1].source && q.content == newQuotes[1].content }
        assertNotNull(newQuote1)
        assertNotNull(newQuote2)

        tagsToImport.forEach { newTag ->
            assertTrue(newQuote1.tags.any { t -> t.name == newTag.name })
            assertTrue(newQuote2.tags.any { t -> t.name == newTag.name })
        }
        assertTrue(newQuote1.tags.containsAll(seedQuotes[0].tags) && newQuote2.tags.containsAll(seedQuotes[1].tags))
    }

    @Test
    fun `import quotes without overwrite does not create new quote if a quote with same source and content exists, and does not create duplicate tag if existing and imported quote share tag of same name`() {
        //arrange
        val tagsToImport = listOf(Tag(10, "TagNew2"))
        val newQuotes = listOf(
            Quote(20, seedQuotes[0].content, seedQuotes[0].source, tagsToImport + seedTags[0]),
        )

        //act
        quoteRepository.importQuotes(newQuotes, false)

        //assert
        val quotes = quoteRepository.getQuotes()
        assertEquals(seedQuotes.size, quotes.size)

        val newQuote1 = quotes.find { q -> q.source == newQuotes[0].source && q.content == newQuotes[0].content }
        assertNotNull(newQuote1)
        tagsToImport.forEach { newTag ->
            newQuote1.tags.any { t -> t.name == newTag.name }
        }
        assertEquals(newQuote1.tags.filter { t -> t.name == seedTags[0].name }.size, 1)
    }
}
