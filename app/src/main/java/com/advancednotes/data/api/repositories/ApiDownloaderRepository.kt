package com.advancednotes.data.api.repositories

import com.advancednotes.domain.models.NoteFile
import java.io.File

interface ApiDownloaderRepository {

    fun downloadFile(
        noteFile: NoteFile,
        noteFileURIEndpoint: String
    ): File?
}