package com.advancednotes.data.database_notes.repositories

import com.advancednotes.data.database_notes.entities.TagEntity

interface TagsRepository {

    fun getTags(): List<TagEntity>
    fun getTagByUUID(uuid: String): TagEntity?
    fun getMaxOrder(): Int?
    fun getTagsModificationDates(): List<String>
    fun insertTag(tagEntity: TagEntity)
    fun updateTag(tagEntity: TagEntity)
    fun updateTagDeletedPermanently(uuid: String, modificationDate: String)
    fun updateTagOrderNumber(uuid: String, orderNumber: Int, modificationDate: String)
    fun deleteAllTags()
    fun deleteTagsDeletedPermanently()
}