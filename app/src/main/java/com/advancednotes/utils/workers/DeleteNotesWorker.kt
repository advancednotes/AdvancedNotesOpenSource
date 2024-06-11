package com.advancednotes.utils.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.advancednotes.data.datastore.getAutoDeleteTrashMode
import com.advancednotes.data.datastore.getLastSyncNotesDate
import com.advancednotes.data.datastore.getLastSyncNotesFilesDate
import com.advancednotes.data.datastore.getLastSyncTagsDate
import com.advancednotes.data.datastore.saveLastDeleteNotesDate
import com.advancednotes.data.datastore.saveLastDeleteNotesFilesDate
import com.advancednotes.data.datastore.saveLastDeleteTagsDate
import com.advancednotes.domain.models.AutoDeleteTrashMode
import com.advancednotes.domain.usecases.AuthenticationUseCase
import com.advancednotes.domain.usecases.NotesFilesUseCase
import com.advancednotes.domain.usecases.NotesUseCase
import com.advancednotes.domain.usecases.TagsUseCase
import com.advancednotes.utils.date.DateHelper.getCurrentDate
import com.advancednotes.utils.files.FileHelper.cleanCacheFilesDirectory
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import java.util.Locale

@HiltWorker
class DeleteNotesWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val authenticationUseCase: AuthenticationUseCase,
    private val notesUseCase: NotesUseCase,
    private val notesFilesUseCase: NotesFilesUseCase,
    private val tagsUseCase: TagsUseCase
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {

            try {
                val hasUser: Boolean = authenticationUseCase.hasUser()

                autoDeleteNotesTrashed()

                cleanCacheFilesDirectory(appContext)

                if (notesAreSync() || !hasUser) {
                    notesUseCase.deleteNotesDeletedPermanently()
                    appContext.saveLastDeleteNotesDate()
                }

                if (tagsAreSync() || !hasUser) {
                    tagsUseCase.deleteTagsDeletedPermanently()
                    appContext.saveLastDeleteTagsDate()
                }

                if (notesFilesAreSync() || !hasUser) {
                    notesFilesUseCase.deleteNotesFilesDeletedPermanently()
                    appContext.saveLastDeleteNotesFilesDate()
                }

                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }

    /**
     * Auto delete notes trashed
     * */
    private fun autoDeleteNotesTrashed() {
        val autoDeleteTrashMode = appContext.getAutoDeleteTrashMode()

        if (autoDeleteTrashMode == AutoDeleteTrashMode.NEVER) {
            return
        }

        val notesOnlyTrashDates = notesUseCase.getNotesTrashDates()

        if (notesOnlyTrashDates.isEmpty()) {
            return
        }

        when (autoDeleteTrashMode) {
            AutoDeleteTrashMode.MONTHLY -> {
                notesOnlyTrashDates.forEach { noteOnlyTrashDate ->
                    noteOnlyTrashDate.trashDate?.let { trashDate ->
                        val calendar = Calendar.getInstance(Locale.getDefault())
                        calendar.time = trashDate

                        calendar.add(Calendar.MONTH, 1)

                        if (calendar.time.before(getCurrentDate())) {
                            notesUseCase.updateNoteDeletedPermanently(noteOnlyTrashDate.uuid)
                        }
                    }
                }
            }

            AutoDeleteTrashMode.SIX_MONTHLY -> {
                notesOnlyTrashDates.forEach { noteOnlyTrashDate ->
                    noteOnlyTrashDate.trashDate?.let { trashDate ->
                        val calendar = Calendar.getInstance(Locale.getDefault())
                        calendar.time = trashDate

                        calendar.add(Calendar.MONTH, 6)

                        if (calendar.time.before(getCurrentDate())) {
                            notesUseCase.updateNoteDeletedPermanently(noteOnlyTrashDate.uuid)
                        }
                    }
                }
            }

            else -> {}
        }
    }

    /**
     * Notes
     * */
    private fun notesAreSync(): Boolean {
        val lastNotesModificationDate: Date? = getLastNotesModificationDate()
        val syncNotesDate: Date? = appContext.getLastSyncNotesDate()

        if (lastNotesModificationDate == null || syncNotesDate == null) {
            return false
        }

        return lastNotesModificationDate.before(syncNotesDate)
    }

    private fun getLastNotesModificationDate(): Date? {
        val dates: List<Date> = notesUseCase.getNotesModificationDates()
        return dates.maxOrNull()
    }

    /**
     * Tags
     * */
    private fun tagsAreSync(): Boolean {
        val lastTagsModificationDate: Date? = getLastTagsModificationDate()
        val syncTagsDate: Date? = appContext.getLastSyncTagsDate()

        if (lastTagsModificationDate == null || syncTagsDate == null) {
            return false
        }

        return lastTagsModificationDate.before(syncTagsDate)
    }

    private fun getLastTagsModificationDate(): Date? {
        val dates: List<Date> = tagsUseCase.getTagsModificationDates()
        return dates.maxOrNull()
    }

    /**
     * Notes Files
     * */
    private fun notesFilesAreSync(): Boolean {
        val lastNotesFilesModificationDate: Date? = getLastNotesFilesModificationDate()
        val syncNotesFilesDate: Date? = appContext.getLastSyncNotesFilesDate()

        if (lastNotesFilesModificationDate == null || syncNotesFilesDate == null) {
            return false
        }

        return lastNotesFilesModificationDate.before(syncNotesFilesDate)
    }

    private fun getLastNotesFilesModificationDate(): Date? {
        val dates: List<Date> = notesFilesUseCase.getNotesFilesModificationDates()
        return dates.maxOrNull()
    }
}