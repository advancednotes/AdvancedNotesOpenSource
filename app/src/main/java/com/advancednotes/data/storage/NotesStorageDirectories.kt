package com.advancednotes.data.storage

import android.content.Context
import java.io.File

object NotesStorageDirectories {

    /**
     * Path: /data/data/com.app_route/notes/$noteUUID/$specificDirectory
     * */
    fun getNoteSpecificDirectory(
        context: Context,
        noteUUID: String,
        specificDirectory: NoteDirectory
    ): File {
        val noteDirectory = getNoteDirectory(context, noteUUID)
        val noteSpecificDirectory = File(noteDirectory, specificDirectory.path)
        if (!noteSpecificDirectory.exists()) noteSpecificDirectory.mkdirs()

        return noteSpecificDirectory
    }

    /**
     * Path: /data/data/com.app_route/notes/$noteUUID
     * */
    fun getNoteDirectory(context: Context, noteUUID: String): File {
        val notesDirectory = getNotesDirectory(context)
        val noteDirectory = File(notesDirectory, noteUUID)
        if (!noteDirectory.exists()) noteDirectory.mkdirs()

        return noteDirectory
    }

    /**
     * Path: /data/data/com.app_route/notes
     * */
    fun getNotesDirectory(context: Context): File {
        val notesDirectory = File(context.filesDir, "notes_files")
        if (!notesDirectory.exists()) notesDirectory.mkdirs()

        return notesDirectory
    }
}