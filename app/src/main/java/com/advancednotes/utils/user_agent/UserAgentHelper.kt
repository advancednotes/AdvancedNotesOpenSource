package com.advancednotes.utils.user_agent

import android.content.Context
import android.os.Build
import javax.inject.Inject

class UserAgentHelper @Inject constructor(
    private val context: Context
) {

    fun generateUserAgent(): String {
        val appPackageName = context.packageName
        val packageInfo = context.packageManager.getPackageInfo(appPackageName, 0)
        val appVersion = packageInfo.versionName

        val osName = "Android"
        val osVersion = Build.VERSION.RELEASE
        val deviceManufacturer = Build.MANUFACTURER
        val deviceModel = Build.MODEL
        val deviceProduct = Build.PRODUCT

        val userAgent = buildString {
            append("$appPackageName/$appVersion ")
            append("($osName $osVersion; $deviceManufacturer $deviceModel; $deviceProduct)")
        }

        return userAgent
    }
}