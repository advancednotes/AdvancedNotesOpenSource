package com.advancednotes.domain.usecases.impl

import com.advancednotes.common.Constants.Companion.EMPTY_UUID
import com.advancednotes.data.database_notes.entities.toDomain
import com.advancednotes.data.database_notes.repositories.TagsRepository
import com.advancednotes.domain.models.Tag
import com.advancednotes.domain.models.toDatabase
import com.advancednotes.domain.usecases.TagsUseCase
import com.advancednotes.utils.date.DateHelper.getCurrentDate
import com.advancednotes.utils.date.DateHelper.parseDateToString
import com.advancednotes.utils.date.DateHelper.parseStringToDate
import com.advancednotes.utils.uuid.UUIDHelper.getRandomUUID
import java.util.Date
import javax.inject.Inject

class TagsUseCaseImpl @Inject constructor(
    private val tagsRepository: TagsRepository
) : TagsUseCase {

    override fun getTags(): List<Tag> {
        return tagsRepository.getTags().map { it.toDomain() }
    }

    override fun getTagByUUID(uuid: String): Tag {
        val tag: Tag? = tagsRepository.getTagByUUID(uuid)?.toDomain()

        return tag ?: Tag()
    }

    override fun getTagsModificationDates(): List<Date> {
        return tagsRepository.getTagsModificationDates().map { parseStringToDate(it) }
    }

    override fun insertTag(tag: Tag) {
        val tagAux: Tag = if (tag.uuid == EMPTY_UUID) {
            tag.copy(
                uuid = getRandomUUID(),
                orderNumber = getMaxOrder() + 1
            )
        } else tag

        tagsRepository.insertTag(
            tagEntity = tagAux.toDatabase()
        )
    }

    override fun updateTag(tag: Tag, fromSynchronize: Boolean) {
        val tagAux: Tag = if (!fromSynchronize) {
            tag.copy(
                id = tag.id,
                modificationDate = getCurrentDate()
            )
        } else tag

        tagsRepository.updateTag(
            tagEntity = tagAux.toDatabase()
        )
    }

    override fun updateTagDeletedPermanently(uuid: String) {
        tagsRepository.updateTagDeletedPermanently(
            uuid = uuid,
            modificationDate = parseDateToString(getCurrentDate())
        )
    }

    override fun updateTagOrderNumber(uuid: String, orderNumber: Int) {
        tagsRepository.updateTagOrderNumber(
            uuid = uuid,
            orderNumber = orderNumber,
            modificationDate = parseDateToString(getCurrentDate())
        )
    }

    override fun deleteAllTags() {
        tagsRepository.deleteAllTags()
    }

    override fun deleteTagsDeletedPermanently() {
        tagsRepository.deleteTagsDeletedPermanently()
    }

    private fun getMaxOrder(): Int {
        return tagsRepository.getMaxOrder() ?: 0
    }
}