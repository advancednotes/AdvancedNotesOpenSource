package com.advancednotes.data.api.repositories

import com.advancednotes.data.api.models.NoteFileModel
import com.advancednotes.data.api.models.NoteModel
import com.advancednotes.data.api.models.SynchronizeNoteFileResponseModel
import com.advancednotes.data.api.models.SynchronizeNoteResponseModel
import com.advancednotes.data.api.models.SynchronizeTagResponseModel
import com.advancednotes.data.api.models.TagModel
import java.io.File

interface ApiSynchronizerRepository {

    fun synchronizeNote(
        noteUUID: String,
        noteToSynchronize: NoteModel?,
        onResponse: (response: SynchronizeNoteResponseModel?) -> Unit,
        onBadRequest: (() -> Unit)? = null,
        onAccessTokenNotValid: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    )

    fun synchronizeNoteFile(
        noteFileUUID: String,
        noteFileToSynchronize: NoteFileModel?,
        fileToSynchronize: File?,
        onResponse: (response: SynchronizeNoteFileResponseModel?) -> Unit,
        onBadRequest: (() -> Unit)? = null,
        onAccessTokenNotValid: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    )

    fun synchronizeTag(
        tagUUID: String,
        tagToSynchronize: TagModel?,
        onResponse: (response: SynchronizeTagResponseModel?) -> Unit,
        onBadRequest: (() -> Unit)? = null,
        onAccessTokenNotValid: (() -> Unit)? = null,
        onFailure: (() -> Unit)? = null
    )
}