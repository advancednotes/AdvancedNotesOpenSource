package com.advancednotes.domain.usecases.impl

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.advancednotes.common.Constants.Companion.EMPTY_ID
import com.advancednotes.common.Constants.Companion.EMPTY_UUID
import com.advancednotes.common.Constants.Companion.FILE_PROVIDER_AUTHORITY
import com.advancednotes.data.database_notes.entities.toDomain
import com.advancednotes.data.database_notes.repositories.NotesFilesRepository
import com.advancednotes.data.storage.NoteDirectory
import com.advancednotes.data.storage.repositories.NotesFilesStorageRepository
import com.advancednotes.domain.models.NoteFile
import com.advancednotes.domain.models.toDatabase
import com.advancednotes.domain.usecases.NotesFilesUseCase
import com.advancednotes.utils.date.DateHelper.getCurrentDate
import com.advancednotes.utils.date.DateHelper.getCurrentDateForFileName
import com.advancednotes.utils.date.DateHelper.parseDateToString
import com.advancednotes.utils.date.DateHelper.parseStringToDate
import com.advancednotes.utils.uuid.UUIDHelper.getRandomUUID
import java.io.File
import java.util.Date
import javax.inject.Inject

class NotesFilesUseCaseImpl @Inject constructor(
    private val notesFilesRepository: NotesFilesRepository,
    private val notesFilesStorageRepository: NotesFilesStorageRepository
) : NotesFilesUseCase {

    override fun duplicateNoteFiles(
        oldNoteUUID: String,
        newNoteUUID: String
    ) {
        val directories: Array<NoteDirectory> = NoteDirectory.values()

        directories.forEach { directory ->
            val notesFilesFromDirectory: List<NoteFile> =
                getNoteFilesFromDirectory(oldNoteUUID, directory)

            notesFilesFromDirectory.forEach { noteFile ->
                val newNoteFile: NoteFile = noteFile.copy(
                    id = EMPTY_ID,
                    uuid = getRandomUUID(),
                    noteUUID = newNoteUUID,
                    fileUUID = getRandomUUID(),
                    fileName = getCurrentDateForFileName(),
                    creationDate = getCurrentDate(),
                    modificationDate = getCurrentDate()
                )

                val file: File? = getFile(noteFile)

                if (file != null) {
                    insertNoteFile(newNoteFile, file)
                }
            }
        }
    }

    override fun getAllNotesFiles(): List<NoteFile> {
        return notesFilesRepository.getAllNotesFiles().map { it.toDomain() }
    }

    override fun getAllNoteFiles(noteUUID: String): List<NoteFile> {
        return notesFilesRepository.getAllNoteFiles(noteUUID).map { it.toDomain() }
    }

    override fun getNoteFilesFromDirectory(
        noteUUID: String,
        directory: NoteDirectory
    ): List<NoteFile> {
        return notesFilesRepository.getNoteFilesFromDirectory(noteUUID, directory.path)
            .map { it.toDomain() }
    }

    override fun getNotesFilesModificationDates(): List<Date> {
        return notesFilesRepository.getNotesFilesModificationDates().map { parseStringToDate(it) }
    }

    override fun getNoteFileByUUID(uuid: String): NoteFile {
        val noteFile: NoteFile? = notesFilesRepository.getNoteFileByUUID(uuid)?.toDomain()

        return noteFile ?: NoteFile()
    }

    override fun getNotesFilesDeletedPermanently(): List<NoteFile> {
        return notesFilesRepository.getNotesFilesDeletedPermanently().map { it.toDomain() }
    }

    override fun getFile(noteFile: NoteFile): File? {
        return notesFilesStorageRepository.getFile(
            noteFile.noteUUID,
            noteFile.directory,
            noteFile.fileUUID,
            noteFile.fileExtension
        )
    }

    override fun getFileUriFromNoteFile(context: Context, noteFile: NoteFile): Uri? {
        val file: File? = getFile(noteFile)

        return if (file != null) {
            FileProvider.getUriForFile(
                context,
                FILE_PROVIDER_AUTHORITY,
                file
            )
        } else null
    }

    override fun insertNoteFile(noteFile: NoteFile, tempFile: File): Long {
        val noteFileAux: NoteFile = if (noteFile.uuid == EMPTY_UUID) {
            noteFile.copy(
                uuid = getRandomUUID(),
                fileUUID = getRandomUUID()
            )
        } else noteFile

        val fileSaved: Boolean = notesFilesStorageRepository.saveFile(
            noteUUID = noteFileAux.noteUUID,
            directory = noteFileAux.directory,
            fileIdentifier = noteFileAux.fileUUID,
            fileExtension = noteFileAux.fileExtension,
            tempFile = tempFile
        )

        val noteFileId = if (fileSaved) {
            notesFilesRepository.insertNoteFile(noteFileAux.toDatabase())
        } else {
            -1
        }
        return noteFileId
    }

    override fun updateNoteFile(noteFile: NoteFile, tempFile: File) {
        notesFilesRepository.updateNoteFile(noteFile.toDatabase())

        deleteNoteFile(noteFile)
        insertNoteFile(noteFile, tempFile)
    }

    override fun updateNoteFileName(
        uuid: String,
        newFileName: String
    ) {
        notesFilesRepository.updateNoteFileName(
            uuid = uuid,
            newFileName = newFileName,
            modificationDate = parseDateToString(getCurrentDate())
        )
    }

    override fun updateAllNoteFilesByNoteUUIDDeletedPermanently(noteUUID: String) {
        val noteFiles: List<NoteFile> = getAllNoteFiles(noteUUID)

        noteFiles.forEach { noteFile ->
            updateNoteFileDeletedPermanently(noteFile.uuid)
        }
    }

    override fun updateNoteFileDeletedPermanently(uuid: String) {
        notesFilesRepository.updateNoteFileDeletedPermanently(
            uuid = uuid,
            modificationDate = parseDateToString(getCurrentDate())
        )
    }

    override fun deleteAllNotesFiles() {
        notesFilesStorageRepository.deleteAllFiles()
        notesFilesRepository.deleteAllNotesFiles()
    }

    override fun deleteNotesFilesDeletedPermanently() {
        val notesFilesDeletedPermanently: List<NoteFile> = getNotesFilesDeletedPermanently()

        notesFilesDeletedPermanently.forEach { noteFile ->
            deleteNoteFile(noteFile)
        }
    }

    override fun deleteNoteFile(noteFile: NoteFile) {
        notesFilesStorageRepository.deleteFile(
            noteUUID = noteFile.noteUUID,
            directory = noteFile.directory,
            fileIdentifier = noteFile.fileUUID,
            fileExtension = noteFile.fileExtension
        )

        notesFilesRepository.deleteNoteFileByUUID(noteFile.uuid)
    }
}