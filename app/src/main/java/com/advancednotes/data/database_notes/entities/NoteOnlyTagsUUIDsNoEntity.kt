package com.advancednotes.data.database_notes.entities

import androidx.room.ColumnInfo
import com.advancednotes.domain.models.NoteOnlyTagsUUIDs
import com.google.gson.Gson
import com.google.gson.GsonBuilder

data class NoteOnlyTagsUUIDsNoEntity(
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "uuid")
    val uuid: String,

    @ColumnInfo(name = "tags_uuids")
    val tagsUUIDs: String?,
)

fun NoteOnlyTagsUUIDsNoEntity.toDomain(): NoteOnlyTagsUUIDs {
    val gson: Gson = GsonBuilder().serializeNulls().create()

    return NoteOnlyTagsUUIDs(
        id = id,
        uuid = uuid,
        tagsUUIDs = if (tagsUUIDs != null) {
            gson.fromJson(tagsUUIDs, Array<String>::class.java).toList()
        } else emptyList(),
    )
}