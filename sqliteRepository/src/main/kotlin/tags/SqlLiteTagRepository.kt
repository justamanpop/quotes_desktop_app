package repository.tags

import Quote
import Tag
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import ports.driven.TagRepository
import kotlin.collections.forEach

class SqlLiteTagRepository(val conn: SQLiteConnection) : TagRepository {
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
        conn.prepare("SELECT * FROM tags WHERE name = ?").use { statement ->
            statement.bindText(1, tag.name)
            while(statement.step()) {
               throw IllegalArgumentException("Tag with name ${tag.name} already exists")
            }
        }

        conn.prepare("INSERT INTO tags(name) VALUES(?)").use { statement ->
            statement.bindText(1, tag.name)
            statement.step()
        }
    }

    override fun updateTag(tag: Tag) {
        conn.prepare("UPDATE tags set name = ? where id = ?").use { statement ->
            statement.bindText(1, tag.name)
            statement.bindInt(2, tag.id)
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

    override fun importTags(tags: List<Tag>, overwrite: Boolean) {
        TODO("Not yet implemented")
    }
}
