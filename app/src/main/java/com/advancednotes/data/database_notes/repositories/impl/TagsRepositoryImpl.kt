package com.advancednotes.data.database_notes.repositories.impl

import com.advancednotes.data.database_notes.dao.TagsDao
import com.advancednotes.data.database_notes.entities.TagEntity
import com.advancednotes.data.database_notes.repositories.TagsRepository
import javax.inject.Inject

class TagsRepositoryImpl @Inject constructor(
    private val tagsDao: TagsDao
) : TagsRepository {

    override fun getTags(): List<TagEntity> {
        return tagsDao.getTags()
    }

    override fun getTagByUUID(uuid: String): TagEntity? {
        return tagsDao.getTagByUUID(
            uuid = uuid
        )
    }

    override fun getMaxOrder(): Int? {
        return tagsDao.getMaxOrder()
    }

    override fun getTagsModificationDates(): List<String> {
        return tagsDao.getTagsModificationDates()
    }

    override fun insertTag(tagEntity: TagEntity) {
        tagsDao.insertTag(
            tagEntity = tagEntity
        )
    }

    override fun updateTag(tagEntity: TagEntity) {
        tagsDao.updateTag(
            tagEntity = tagEntity
        )
    }

    override fun updateTagDeletedPermanently(uuid: String, modificationDate: String) {
        tagsDao.updateTagDeletedPermanently(uuid, modificationDate)
    }

    override fun updateTagOrderNumber(uuid: String, orderNumber: Int, modificationDate: String) {
        tagsDao.updateTagOrderNumber(uuid, orderNumber, modificationDate)
    }

    override fun deleteAllTags() {
        tagsDao.deleteAllTags()
    }

    override fun deleteTagsDeletedPermanently() {
        tagsDao.deleteTagsDeletedPermanently()
    }
}