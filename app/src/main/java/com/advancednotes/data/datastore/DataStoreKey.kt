package com.advancednotes.data.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.advancednotes.domain.models.AutoDeleteTrashMode
import com.advancednotes.domain.models.UiMode

sealed class DataStoreKey<T>(val key: Preferences.Key<T>, val defaultValue: T) {

    object ForceUiMode : DataStoreKey<String>(
        key = stringPreferencesKey(name = "ui_mode"),
        defaultValue = UiMode.DEVICE.value
    )

    object UseDynamicColors : DataStoreKey<Boolean>(
        key = booleanPreferencesKey(name = "use_dynamic_colors"),
        defaultValue = false
    )

    object OrderBy : DataStoreKey<String>(
        key = stringPreferencesKey(name = "order_by"),
        defaultValue = "DESC"
    )

    object RemindersNotifications : DataStoreKey<String>(
        key = stringPreferencesKey(name = "reminders_notifications"),
        defaultValue = "[]"
    )

    object StickyNotesNotifications : DataStoreKey<String>(
        key = stringPreferencesKey(name = "sticky_notes_notifications"),
        defaultValue = "[]"
    )

    object LastSyncNotesDate : DataStoreKey<String>(
        key = stringPreferencesKey(name = "last_sync_notes_date"),
        defaultValue = ""
    )

    object LastSyncNotesFilesDate : DataStoreKey<String>(
        key = stringPreferencesKey(name = "last_sync_notes_files_date"),
        defaultValue = ""
    )

    object LastSyncTagsDate : DataStoreKey<String>(
        key = stringPreferencesKey(name = "last_sync_tags_date"),
        defaultValue = ""
    )

    object LastDeleteNotesDate : DataStoreKey<String>(
        key = stringPreferencesKey(name = "last_delete_notes_date"),
        defaultValue = ""
    )

    object LastDeleteNotesFilesDate : DataStoreKey<String>(
        key = stringPreferencesKey(name = "last_delete_notes_files_date"),
        defaultValue = ""
    )

    object LastDeleteTagsDate : DataStoreKey<String>(
        key = stringPreferencesKey(name = "last_delete_tags_date"),
        defaultValue = ""
    )

    object AutoDeleteTrash : DataStoreKey<String>(
        key = stringPreferencesKey(name = "auto_delete_trash"),
        defaultValue = AutoDeleteTrashMode.NEVER.value
    )
}