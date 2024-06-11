package com.advancednotes.utils.logs

import android.util.Log

object LogsHelper {

    private const val APP_LOGS: String = "app_logs"

    fun logd(message: String, tag: String = APP_LOGS) {
        Log.d(tag, message)
    }

    fun loge(message: String, tag: String = APP_LOGS) {
        Log.e(tag, message)
    }
}