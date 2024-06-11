package com.advancednotes.domain.models

import androidx.annotation.Keep
import com.advancednotes.common.Constants.Companion.EMPTY_ID
import com.advancednotes.common.Constants.Companion.EMPTY_UUID
import com.advancednotes.common.Constants.Companion.WITHOUT_TAG_UUID
import com.advancednotes.data.api.models.TagModel
import com.advancednotes.data.database_notes.entities.TagEntity
import com.advancednotes.utils.date.DateHelper.getCurrentDate
import com.advancednotes.utils.date.DateHelper.parseDateToString
import java.util.Date

@Keep
data class Tag(
    val id: Long = EMPTY_ID,
    val uuid: String = EMPTY_UUID,
    val name: String = "",
    val colorHex: String? = null,
    val orderNumber: Int = 0,
    val creationDate: Date = getCurrentDate(),
    val modificationDate: Date = getCurrentDate(),
    val deletedPermanently: Boolean = false
)

fun Tag.toApi(): TagModel {
    return TagModel(
        id = id,
        uuid = uuid,
        name = name,
        colorHex = colorHex,
        orderNumber = orderNumber,
        creationDate = parseDateToString(creationDate),
        modificationDate = parseDateToString(modificationDate),
        deletedPermanently = deletedPermanently
    )
}

fun Tag.toDatabase(): TagEntity {
    return TagEntity(
        id = id,
        uuid = uuid,
        name = name,
        colorHex = colorHex,
        orderNumber = orderNumber,
        creationDate = parseDateToString(creationDate),
        modificationDate = parseDateToString(modificationDate),
        deletedPermanently = deletedPermanently
    )
}

fun Tag.getUncategorized(name: String): Tag {
    return Tag(
        uuid = WITHOUT_TAG_UUID,
        name = name,
        orderNumber = 999
    )
}

fun Tag.isEmpty(): Boolean {
    return id == EMPTY_ID &&
            uuid == EMPTY_UUID &&
            name.isEmpty() &&
            colorHex.isNullOrEmpty()
}