package com.advancednotes.ui.screens

import com.advancednotes.R

sealed class Screen(val route: String, val labelResource: Int) {
    object LoginScreen : Screen("login_screen", R.string.screen_login_label)
    object RegisterScreen : Screen("register_screen", R.string.screen_register_label)
    object SettingsScreen : Screen("settings_screen", R.string.screen_settings_label)
    object UserScreen : Screen("user_screen", R.string.screen_user_label)
    object NotesScreen : Screen("notes_screen", R.string.screen_notes_label)
    object ArchivedNotesScreen : Screen("archived_notes_screen", R.string.screen_archived_notes_label)
    object TrashedNotesScreen : Screen("notes_trashed_screen", R.string.screen_trashed_notes_label)
    object SingleNoteScreen : Screen("single_note_screen", R.string.screen_single_note_label)
    object NoteGalleryScreen : Screen("note_gallery_screen", R.string.screen_gallery_label)
    object TagsScreen : Screen("tags_screen", R.string.screen_tags_label)
    object SingleTagScreen : Screen("single_tag_screen", R.string.screen_single_tag_label)
}