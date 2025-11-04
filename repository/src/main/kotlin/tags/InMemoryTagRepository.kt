package repository.tags

import Tag
import ports.driven.TagRepository

val fakeTags = mutableMapOf<String, Tag>(
    "Malazan" to Tag(1, "Malazan"),
    "Inspirational" to Tag(2, "Inspirational"),
    "Defiance" to Tag(3, "Defiance"),
    "Shakespeare" to Tag(4, "Shakespeare"),
    "Congruence" to Tag(5, "Congruence"),
    "Purpose" to Tag(6, "Purpose"),
    "No one cares" to Tag(7, "No one cares"),
    "Nietzsche" to Tag(8, "Nietzsche"),
    "Dorian Grey" to Tag(9, "Dorian Grey"),
    "Beauty" to Tag(10, "Beauty"),
)

class InMemoryTagRepository: TagRepository {
    private var maxId = 10;

    override fun getTags(): List<Tag> {
        return fakeTags.values.toList().sortedBy { t -> t.name }
    }

    override fun addTag(tag: Tag) {
        maxId +=1;
        fakeTags[tag.name] = tag.copy(id = maxId)
    }

    override fun updateTag(tag: Tag) {
        val entry = fakeTags.values.find { t -> t.id == tag.id }
        if (entry != null) {
            fakeTags[tag.name] = tag
            fakeTags.remove(entry.name)
        }
    }

    override fun deleteTag(tagId: Int) {
        val entry = fakeTags.values.find { t -> t.id == tagId }
        if (entry != null) {
            fakeTags.remove(entry.name)
        }
    }
}
