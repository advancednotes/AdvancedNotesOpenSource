package com.advancednotes.ui.screens.settings

import com.advancednotes.domain.models.AutoDeleteTrashMode
import com.advancednotes.domain.models.UiMode

data class SettingsState(
    var uiMode: UiMode = UiMode.DEVICE,
    var autoDeleteTrashMode: AutoDeleteTrashMode = AutoDeleteTrashMode.NEVER,
)