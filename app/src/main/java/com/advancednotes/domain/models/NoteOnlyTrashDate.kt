package com.advancednotes.domain.models

import androidx.annotation.Keep
import com.advancednotes.common.Constants.Companion.EMPTY_ID
import com.advancednotes.common.Constants.Companion.EMPTY_UUID
import java.util.Date

@Keep
data class NoteOnlyTrashDate(
    val id: Long = EMPTY_ID,
    val uuid: String = EMPTY_UUID,
    val trashDate: Date? = null,
)