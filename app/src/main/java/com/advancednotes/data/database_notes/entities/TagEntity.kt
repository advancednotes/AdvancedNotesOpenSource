package com.advancednotes.data.database_notes.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.advancednotes.domain.models.Tag
import com.advancednotes.utils.date.DateHelper.parseStringToDate

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "uuid")
    val uuid: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "color_hex")
    val colorHex: String?,

    @ColumnInfo(name = "order_number")
    val orderNumber: Int,

    @ColumnInfo(name = "creation_date")
    val creationDate: String,

    @ColumnInfo(name = "modification_date")
    val modificationDate: String,

    @ColumnInfo(name = "deleted_permanently")
    val deletedPermanently: Boolean
)

fun TagEntity.toDomain(): Tag {
    return Tag(
        id = id,
        uuid = uuid,
        name = name,
        colorHex = colorHex,
        orderNumber = orderNumber,
        creationDate = parseStringToDate(creationDate),
        modificationDate = parseStringToDate(modificationDate),
        deletedPermanently = deletedPermanently
    )
}