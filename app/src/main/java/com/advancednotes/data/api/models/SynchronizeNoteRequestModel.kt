package com.advancednotes.data.api.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SynchronizeNoteRequestModel(
    @SerializedName("noteUUID") val noteUUID: String,
    @SerializedName("noteToSynchronize") val noteToSynchronize: NoteModel?
)