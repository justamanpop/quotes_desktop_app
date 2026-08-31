package repository.tags

import Tag
import ports.driven.TagRepository

class InMemoryTagRepository(initialTags: List<Tag> = emptyList()): TagRepository {
    private var fakeTags = initialTags.associateBy { t -> t.name }.toMutableMap()
    private var maxId = fakeTags.size

    override fun getTags(): List<Tag> {
        return fakeTags.values.toList().sortedBy { t -> t.name }
    }

    override fun addTag(tag: Tag) {
        if (fakeTags.containsKey(tag.name)) {
            throw IllegalArgumentException("Tag with name ${tag.name} already exists")
        }
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

    override fun importTags(tags: List<Tag>, overwrite: Boolean) {
        if(overwrite) {
            importTagsWithOverride(tags)
            return
        }

        tags.forEach {
            tagToInsert ->
                if(!fakeTags.containsKey(tagToInsert.name)) {
                    maxId +=1;
                    fakeTags[tagToInsert.name] = tagToInsert.copy(id = maxId)
                }
        }
    }

    private fun importTagsWithOverride(tags: List<Tag>) {
        fakeTags.clear()
        maxId = 0
        tags.forEach {
            tagToInsert ->
            maxId +=1;
            fakeTags[tagToInsert.name] = tagToInsert.copy(id = maxId)
        }
    }
}
