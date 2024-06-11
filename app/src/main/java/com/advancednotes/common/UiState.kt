package com.advancednotes.common

import com.advancednotes.domain.models.UiMode

data class UIState(
    var uiMode: UiMode = UiMode.DEVICE,
    var useDynamicColors: Boolean = false
)