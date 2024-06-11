package com.advancednotes.utils.permissions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.isGranted(): Boolean {
    return status.isGranted
}

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.shouldShowRationale(): Boolean {
    return !status.isGranted && status.shouldShowRationale
}

@OptIn(ExperimentalPermissionsApi::class)
fun MultiplePermissionsState.allPermissionsGranted(): Boolean {
    return allPermissionsGranted
}

@OptIn(ExperimentalPermissionsApi::class)
fun MultiplePermissionsState.shouldShowRationale(): Boolean {
    return shouldShowRationale
}

fun Context.openAppSystemSettings() {
    startActivity(
        Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", packageName, null)
        }
    )
}