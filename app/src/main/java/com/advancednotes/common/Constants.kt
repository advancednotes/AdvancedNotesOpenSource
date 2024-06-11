package com.advancednotes.common

import androidx.compose.ui.unit.dp

class Constants {

    companion object {
        const val EMPTY_ID: Long = 0L
        const val EMPTY_UUID: String = "00000000-0000-0000-0000-000000000000"
        const val WITHOUT_TAG_UUID: String = "00000000-0000-0000-0000-00000tag0null"
        const val FILE_PROVIDER_AUTHORITY = "com.advancednotes.fileProvider"

        const val ACTION_START_SERVICE = "com.advancednotes.action.ACTION_START_SERVICE"
        const val ACTION_STOP_SERVICE = "com.advancednotes.action.ACTION_STOP_SERVICE"
        const val STOP_SERVICE_REQUEST_CODE: Int = 2323

        const val ACTION_DELETE_NOTIFICATION = "com.advancednotes.action.DELETE_NOTIFICATION"

        const val AUTH_ENABLED: Boolean = false

        const val ANIMATED_TEXT_DELAY = 30L

        val HORIZONTAL_PADDING = 16.dp
        val VERTICAL_PADDING = 16.dp

        val CONTENT_PADDING_DEFAULT = 10.dp
    }
}