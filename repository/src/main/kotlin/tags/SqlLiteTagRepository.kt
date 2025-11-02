package repository.tags

import Quote
import Tag
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import ports.driven.TagRepository

class SqlLiteTagRepository(dbName: String) : TagRepository {
    val conn: SQLiteConnection = BundledSQLiteDriver().open(dbName)

    override fun getTags(): List<Tag> {
        val tags = mutableListOf<Tag>()
        conn.prepare(
            "SELECT * FROM tags"
        ).use { statement ->
            while (statement.step()) {
                tags.add(Tag(statement.getInt(0), statement.getText(1)))
            }
        }
        return tags
    }

    override fun addTag(tag: Tag) {
        conn.prepare("INSERT INTO tags(name) VALUES(?)").use { statement ->
            statement.bindText(1, tag.name)
            statement.step()
        }
    }

    override fun updateTag(tagId: Int, newName: String) {
        conn.prepare("UPDATE tags set name = ? where id = ?").use { statement ->
            statement.bindText(1, newName)
            statement.bindInt(2, tagId)
            statement.step()
        }
    }

    override fun deleteTag(tagId: Int) {
        conn.execSQL("BEGIN TRANSACTION;")
        try {
            conn.prepare("DELETE FROM quote_tag_mapping where tag_id = ?").use { statement ->
                statement.bindInt(1, tagId)
                statement.step()
            }
            conn.prepare("DELETE FROM tags where id = ?").use { statement ->
                statement.bindInt(1, tagId)
                statement.step()
            }
            conn.execSQL("COMMIT;")
        } catch (e: Exception) {
            conn.execSQL("ROLLBACK;")
            throw e;
        }
    }
}
