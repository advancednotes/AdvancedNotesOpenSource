package com.advancednotes.domain.usecases

import com.advancednotes.domain.models.Tag
import java.util.Date

interface TagsUseCase {

    fun getTags(): List<Tag>
    fun getTagByUUID(uuid: String): Tag

    fun getTagsModificationDates(): List<Date>

    fun insertTag(tag: Tag)

    fun updateTag(tag: Tag, fromSynchronize: Boolean = false)
    fun updateTagDeletedPermanently(uuid: String)
    fun updateTagOrderNumber(uuid: String, orderNumber: Int)

    fun deleteAllTags()
    fun deleteTagsDeletedPermanently()
}