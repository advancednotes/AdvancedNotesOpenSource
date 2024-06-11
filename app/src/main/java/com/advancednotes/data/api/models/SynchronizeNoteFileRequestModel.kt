package com.advancednotes.data.api.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SynchronizeNoteFileRequestModel(
    @SerializedName("noteFileUUID") val noteFileUUID: String,
    @SerializedName("noteFileToSynchronize") val noteFileToSynchronize: NoteFileModel?
)