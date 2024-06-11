package com.advancednotes.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.advancednotes.domain.models.AutoDeleteTrashMode
import com.advancednotes.domain.models.ReminderNotification
import com.advancednotes.domain.models.StickyNoteNotification
import com.advancednotes.domain.models.UiMode
import com.advancednotes.utils.date.DateHelper.getCurrentDate
import com.advancednotes.utils.date.DateHelper.parseDateToString
import com.advancednotes.utils.date.DateHelper.parseStringToDate
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import java.lang.reflect.Type
import java.util.Date

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * UI MODE
 * */
fun Context.getUIMode(): UiMode {
    val uiMode: String = runBlocking {
        dataStore.data.firstOrNull()?.get(DataStoreKey.ForceUiMode.key)
            ?: DataStoreKey.ForceUiMode.defaultValue
    }

    return UiMode.values().find { it.value == uiMode } ?: UiMode.DEVICE
}

fun Context.saveUIMode(uiMode: UiMode): Preferences {
    val preferences: Preferences = runBlocking {
        dataStore.edit {
            it[DataStoreKey.ForceUiMode.key] = uiMode.value
        }
    }

    return preferences
}

/**
 * USE DYNAMIC COLORS
 * */
fun Context.getUseDynamicColors(): Boolean {
    val useDynamicColors: Boolean = runBlocking {
        dataStore.data.firstOrNull()?.get(DataStoreKey.UseDynamicColors.key)
            ?: DataStoreKey.UseDynamicColors.defaultValue
    }

    return useDynamicColors
}

fun Context.saveUseDynamicColors(useDynamicColors: Boolean): Preferences {
    val preferences: Preferences = runBlocking {
        dataStore.edit {
            it[DataStoreKey.UseDynamicColors.key] = useDynamicColors
        }
    }

    return preferences
}

/**
 * ORDER BY (Unused)
 * */
fun Context.getOrderBy(): String {
    val orderBy: String = runBlocking {
        dataStore.data.firstOrNull()?.get(DataStoreKey.OrderBy.key)
            ?: DataStoreKey.ForceUiMode.defaultValue
    }

    return orderBy
}

fun Context.saveOrderBy(orderBy: String): Preferences {
    val preferences: Preferences = runBlocking {
        dataStore.edit {
            it[DataStoreKey.OrderBy.key] = orderBy
        }
    }

    return preferences
}

/**
 * REMINDER NOTIFICATIONS
 * */
fun Context.getRemindersNotifications(): List<ReminderNotification> {
    val gson: Gson = GsonBuilder().serializeNulls().create()

    val reminderNotificationsJson: String = runBlocking {
        dataStore.data.firstOrNull()?.get(DataStoreKey.RemindersNotifications.key)
            ?: DataStoreKey.RemindersNotifications.defaultValue
    }

    val typeToken: Type = object : TypeToken<List<ReminderNotification>>() {}.type

    return gson.fromJson(reminderNotificationsJson, typeToken)
}

fun Context.saveReminderNotification(reminderNotification: ReminderNotification): Preferences {
    val gson: Gson = GsonBuilder().serializeNulls().create()

    val reminderNotifications: MutableList<ReminderNotification> =
        getRemindersNotifications().toMutableList()

    if (!reminderNotifications.contains(reminderNotification)) {
        reminderNotifications.add(reminderNotification)
    }

    val preferences: Preferences = runBlocking {
        dataStore.edit {
            it[DataStoreKey.RemindersNotifications.key] = gson.toJson(reminderNotifications)
        }
    }

    return preferences
}

fun Context.deleteReminderNotification(reminderNotification: ReminderNotification): Preferences {
    val gson: Gson = GsonBuilder().serializeNulls().create()

    val reminderNotifications: MutableList<ReminderNotification> =
        getRemindersNotifications().toMutableList()

    if (reminderNotifications.contains(reminderNotification)) {
        reminderNotifications.remove(reminderNotification)
    }

    val preferences: Preferences = runBlocking {
        dataStore.edit {
            it[DataStoreKey.RemindersNotifications.key] = gson.toJson(reminderNotifications)
        }
    }

    return preferences
}

/**
 * STICKY NOTES NOTIFICATIONS
 * */
fun Context.getStickyNotesNotifications(): List<StickyNoteNotification> {
    val gson: Gson = GsonBuilder().serializeNulls().create()

    val stickyNotesNotifications: String = runBlocking {
        dataStore.data.firstOrNull()?.get(DataStoreKey.StickyNotesNotifications.key)
            ?: DataStoreKey.StickyNotesNotifications.defaultValue
    }

    val typeToken: Type = object : TypeToken<List<StickyNoteNotification>>() {}.type

    return gson.fromJson(stickyNotesNotifications, typeToken)
}

fun Context.getStickyNoteNotificationByNoteUUID(noteUUID: String): StickyNoteNotification? {
    val gson: Gson = GsonBuilder().serializeNulls().create()

    val stickyNotesNotificationsJson: String = runBlocking {
        dataStore.data.firstOrNull()?.get(DataStoreKey.StickyNotesNotifications.key)
            ?: DataStoreKey.StickyNotesNotifications.defaultValue
    }

    val typeToken: Type = object : TypeToken<List<StickyNoteNotification>>() {}.type

    val stickyNotesNotifications: List<StickyNoteNotification> =
        gson.fromJson(stickyNotesNotificationsJson, typeToken)

    val indexOfStickyNoteNotification: Int =
        stickyNotesNotifications.indexOfFirst { it.noteUUID == noteUUID }

    if (indexOfStickyNoteNotification != -1) {
        return stickyNotesNotifications[indexOfStickyNoteNotification]
    }

    return null
}

fun Context.saveStickyNoteNotification(stickyNoteNotification: StickyNoteNotification): Preferences {
    val gson: Gson = GsonBuilder().serializeNulls().create()

    val stickyNotesNotifications: MutableList<StickyNoteNotification> =
        getStickyNotesNotifications().toMutableList()

    if (!stickyNotesNotifications.contains(stickyNoteNotification)) {
        stickyNotesNotifications.add(stickyNoteNotification)
    }

    val preferences: Preferences = runBlocking {
        dataStore.edit {
            it[DataStoreKey.StickyNotesNotifications.key] = gson.toJson(stickyNotesNotifications)
        }
    }

    return preferences
}

fun Context.deleteStickyNoteNotification(stickyNoteNotification: StickyNoteNotification): Preferences {
    val gson: Gson = GsonBuilder().serializeNulls().create()

    val stickyNotesNotifications: MutableList<StickyNoteNotification> =
        getStickyNotesNotifications().toMutableList()

    if (stickyNotesNotifications.contains(stickyNoteNotification)) {
        stickyNotesNotifications.remove(stickyNoteNotification)
    }

    val preferences: Preferences = runBlocking {
        dataStore.edit {
            it[DataStoreKey.StickyNotesNotifications.key] = gson.toJson(stickyNotesNotifications)
        }
    }

    return preferences
}

/**
 * LAST SYNC NOTES DATE
 * */
fun Context.getLastSyncNotesDate(): Date? {
    val lastSyncNotesDate: String = runBlocking {
        dataStore.data.firstOrNull()?.get(DataStoreKey.LastSyncNotesDate.key)
            ?: DataStoreKey.LastSyncNotesDate.defaultValue
    }

    return if (lastSyncNotesDate.isNotEmpty()) {
        parseStringToDate(lastSyncNotesDate)
    } else null
}

fun Context.saveLastSyncNotesDate(): Preferences {
    val preferences: Preferences = runBlocking {
        dataStore.edit {
            it[DataStoreKey.LastSyncNotesDate.key] = parseDateToString(getCurrentDate())
        }
    }

    return preferences
}

/**
 * LAST SYNC NOTES FILES DATE
 * */
fun Context.getLastSyncNotesFilesDate(): Date? {
    val lastSyncNotesFilesDate: String = runBlocking {
        dataStore.data.firstOrNull()?.get(DataStoreKey.LastSyncNotesFilesDate.key)
            ?: DataStoreKey.LastSyncNotesFilesDate.defaultValue
    }

    return if (lastSyncNotesFilesDate.isNotEmpty()) {
        parseStringToDate(lastSyncNotesFilesDate)
    } else null
}

fun Context.saveLastSyncNotesFilesDate(): Preferences {
    val preferences: Preferences = runBlocking {
        dataStore.edit {
            it[DataStoreKey.LastSyncNotesFilesDate.key] = parseDateToString(getCurrentDate())
        }
    }

    return preferences
}

/**
 * LAST SYNC TAGS DATE
 * */
fun Context.getLastSyncTagsDate(): Date? {
    val lastSyncTagsDate: String = runBlocking {
        dataStore.data.firstOrNull()?.get(DataStoreKey.LastSyncTagsDate.key)
            ?: DataStoreKey.LastSyncTagsDate.defaultValue
    }

    return if (lastSyncTagsDate.isNotEmpty()) {
        parseStringToDate(lastSyncTagsDate)
    } else null
}

fun Context.saveLastSyncTagsDate(): Preferences {
    val preferences: Preferences = runBlocking {
        dataStore.edit {
            it[DataStoreKey.LastSyncTagsDate.key] = parseDateToString(getCurrentDate())
        }
    }

    return preferences
}

/**
 * LAST DELETE NOTES DATE
 * */
fun Context.getLastDeleteNotesDate(): Date? {
    val lastDeleteNotesDate: String = runBlocking {
        dataStore.data.firstOrNull()?.get(DataStoreKey.LastDeleteNotesDate.key)
            ?: DataStoreKey.LastDeleteNotesDate.defaultValue
    }

    return if (lastDeleteNotesDate.isNotEmpty()) {
        parseStringToDate(lastDeleteNotesDate)
    } else null
}

fun Context.saveLastDeleteNotesDate(): Preferences {
    val preferences: Preferences = runBlocking {
        dataStore.edit {
            it[DataStoreKey.LastDeleteNotesDate.key] = parseDateToString(getCurrentDate())
        }
    }

    return preferences
}

/**
 * LAST DELETE NOTES FILES DATE
 * */
fun Context.getLastDeleteNotesFilesDate(): Date? {
    val lastDeleteNotesFilesDate: String = runBlocking {
        dataStore.data.firstOrNull()?.get(DataStoreKey.LastDeleteNotesFilesDate.key)
            ?: DataStoreKey.LastDeleteNotesFilesDate.defaultValue
    }

    return if (lastDeleteNotesFilesDate.isNotEmpty()) {
        parseStringToDate(lastDeleteNotesFilesDate)
    } else null
}

fun Context.saveLastDeleteNotesFilesDate(): Preferences {
    val preferences: Preferences = runBlocking {
        dataStore.edit {
            it[DataStoreKey.LastDeleteNotesFilesDate.key] = parseDateToString(getCurrentDate())
        }
    }

    return preferences
}

/**
 * LAST DELETE TAGS DATE
 * */
fun Context.getLastDeleteTagsDate(): Date? {
    val lastDeleteTagsDate: String = runBlocking {
        dataStore.data.firstOrNull()?.get(DataStoreKey.LastDeleteTagsDate.key)
            ?: DataStoreKey.LastDeleteTagsDate.defaultValue
    }

    return if (lastDeleteTagsDate.isNotEmpty()) {
        parseStringToDate(lastDeleteTagsDate)
    } else null
}

fun Context.saveLastDeleteTagsDate(): Preferences {
    val preferences: Preferences = runBlocking {
        dataStore.edit {
            it[DataStoreKey.LastDeleteTagsDate.key] = parseDateToString(getCurrentDate())
        }
    }

    return preferences
}

/**
 * UI MODE
 * */
fun Context.getAutoDeleteTrashMode(): AutoDeleteTrashMode {
    val autoDeleteNotesTrashedMode: String = runBlocking {
        dataStore.data.firstOrNull()?.get(DataStoreKey.AutoDeleteTrash.key)
            ?: DataStoreKey.AutoDeleteTrash.defaultValue
    }

    return AutoDeleteTrashMode.values().find { it.value == autoDeleteNotesTrashedMode }!!
}

fun Context.saveAutoDeleteTrashMode(autoDeleteTrashMode: AutoDeleteTrashMode): Preferences {
    val preferences: Preferences = runBlocking {
        dataStore.edit {
            it[DataStoreKey.AutoDeleteTrash.key] = autoDeleteTrashMode.value
        }
    }

    return preferences
}
