package com.advancednotes.domain.models

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

class MySnackbarVisuals(
    override val message: String,
    val isError: Boolean = false,
    override val actionLabel: String = "",
    override val withDismissAction: Boolean = actionLabel.isNotEmpty(),
    override val duration: SnackbarDuration = SnackbarDuration.Short
) : SnackbarVisuals