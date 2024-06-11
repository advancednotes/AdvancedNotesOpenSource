package com.advancednotes.common

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.advancednotes.data.datastore.getUIMode
import com.advancednotes.data.datastore.getUseDynamicColors
import com.advancednotes.domain.models.UiMode

object UIModeState {
    var uiState: MutableState<UIState> = mutableStateOf(UIState())

    fun initUIState(context: Context) {
        val uiMode: UiMode = context.getUIMode()
        val useDynamicColors: Boolean = context.getUseDynamicColors()

        uiState.value = uiState.value.copy(
            uiMode = uiMode,
            useDynamicColors = useDynamicColors
        )
    }
}