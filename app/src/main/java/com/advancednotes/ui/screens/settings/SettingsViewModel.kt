package com.advancednotes.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancednotes.common.UIModeState.uiState
import com.advancednotes.data.datastore.getAutoDeleteTrashMode
import com.advancednotes.data.datastore.saveAutoDeleteTrashMode
import com.advancednotes.data.datastore.saveUIMode
import com.advancednotes.data.datastore.saveUseDynamicColors
import com.advancednotes.domain.models.AutoDeleteTrashMode
import com.advancednotes.domain.models.UiMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()

    fun setUiMode(context: Context, uiMode: UiMode) {
        viewModelScope.launch(Dispatchers.Default) {
            context.saveUIMode(uiMode)
            uiState.value = uiState.value.copy(
                uiMode = uiMode
            )
        }
    }

    fun setUseDynamicColors(context: Context, useDynamicColors: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            context.saveUseDynamicColors(useDynamicColors)
            uiState.value = uiState.value.copy(
                useDynamicColors = useDynamicColors
            )
        }
    }

    fun getAutoDeleteTrashMode(context: Context) {
        viewModelScope.launch(Dispatchers.Default) {
            val autoDeleteTrashMode = context.getAutoDeleteTrashMode()
            _settingsState.value = _settingsState.value.copy(
                autoDeleteTrashMode = autoDeleteTrashMode
            )
        }
    }

    fun setAutoDeleteTrashMode(context: Context, autoDeleteTrashMode: AutoDeleteTrashMode) {
        viewModelScope.launch(Dispatchers.Default) {
            context.saveAutoDeleteTrashMode(autoDeleteTrashMode)
            _settingsState.value = _settingsState.value.copy(
                autoDeleteTrashMode = autoDeleteTrashMode
            )
        }
    }
}