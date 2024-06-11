package com.advancednotes.ui.screens.main

import android.content.Context
import android.content.Intent
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.advancednotes.domain.models.AdvancedFabMenuItem
import com.advancednotes.domain.models.MySnackbarVisuals
import com.advancednotes.domain.models.TopAppBarItem
import com.advancednotes.domain.models.User
import com.advancednotes.domain.usecases.AuthenticationUseCase
import com.advancednotes.domain.usecases.NotesUseCase
import com.advancednotes.domain.usecases.PreSynchronizerUseCase
import com.advancednotes.domain.usecases.UsersUseCase
import com.advancednotes.ui.components.advanced_fab_menu.AdvancedFabMenuState
import com.advancednotes.ui.components.top_app_bar.MyTopAppBarState
import com.advancednotes.utils.network.NetworkHelper.isNetworkConnected
import com.advancednotes.utils.services.SetRemindersService
import com.advancednotes.utils.workers.WorkerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val authenticationUseCase: AuthenticationUseCase,
    private val usersUseCase: UsersUseCase,
    private val notesUseCase: NotesUseCase,
    private val preSynchronizerUseCase: PreSynchronizerUseCase,
    private val workerManager: WorkerManager
) : ViewModel() {

    companion object {
        val notesCountState: MutableStateFlow<NotesCountState> = MutableStateFlow(NotesCountState())

        fun setNotesCount(
            countNotes: Int,
            countArchivedNotes: Int,
            countTrashedNotes: Int,
        ) {
            notesCountState.value = NotesCountState(
                notesCount = countNotes,
                notesArchivedCount = countArchivedNotes,
                notesTrashedCount = countTrashedNotes
            )
        }
    }

    private val _currentScreenRouteState: MutableStateFlow<String> = MutableStateFlow("")
    val currentScreenRouteState: StateFlow<String> = _currentScreenRouteState.asStateFlow()

    private val _synchronizeState: MutableStateFlow<SynchronizeState> =
        MutableStateFlow(SynchronizeState())
    val synchronizeState: StateFlow<SynchronizeState> = _synchronizeState.asStateFlow()

    private val _userState: MutableStateFlow<User?> = MutableStateFlow(null)
    val userState: StateFlow<User?> = _userState.asStateFlow()

    private val _topAppBarState: MutableStateFlow<MyTopAppBarState> =
        MutableStateFlow(MyTopAppBarState(navigationBackAction = {}))
    val topAppBarState: StateFlow<MyTopAppBarState> = _topAppBarState.asStateFlow()

    private val _advancedFabMenuState: MutableStateFlow<AdvancedFabMenuState> =
        MutableStateFlow(AdvancedFabMenuState())
    val advancedFabMenuState: StateFlow<AdvancedFabMenuState> = _advancedFabMenuState.asStateFlow()

    private val _snackbarHostState: MutableStateFlow<SnackbarHostState> =
        MutableStateFlow(SnackbarHostState())
    val snackbarHostState: StateFlow<SnackbarHostState> = _snackbarHostState.asStateFlow()

    fun syncronize(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            if (
                isNetworkConnected(context) &&
                authenticationUseCase.hasAccessToken() &&
                !_synchronizeState.value.isSynchronized
            ) {
                _synchronizeState.emit(
                    _synchronizeState.value.copy(
                        isSynchronizing = true
                    )
                )

                var completedCount = 0

                fun checkAllCompleted() {
                    completedCount++
                    if (completedCount == 3) {
                        viewModelScope.launch {
                            _synchronizeState.emit(
                                _synchronizeState.value.copy(
                                    isSynchronizing = false,
                                    isSynchronized = true
                                )
                            )
                        }
                    }
                }

                preSynchronizerUseCase.preSynchronizeNotes(
                    onComplete = {
                        checkAllCompleted()
                    },
                    onFailure = {
                        // Here add sync error notification when all is completed
                        checkAllCompleted()
                    }
                )

                preSynchronizerUseCase.preSynchronizeTags(
                    onComplete = {
                        checkAllCompleted()
                    },
                    onFailure = {
                        // Here add sync error notification when all is completed
                        checkAllCompleted()
                    }
                )

                preSynchronizerUseCase.preSynchronizeNotesFiles(
                    onComplete = {
                        checkAllCompleted()
                    },
                    onFailure = {
                        // Here add sync error notification when all is completed
                        checkAllCompleted()
                    }
                )
            }
        }
    }

    fun initUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val user: User? = usersUseCase.getUser()

            withContext(Dispatchers.Default) {
                setUserState(user)
            }
        }
    }

    fun initNotesCount() {
        viewModelScope.launch(Dispatchers.IO) {
            notesUseCase.refreshNotesCountState()
        }
    }

    fun setCurrentRouteState(route: String) {
        viewModelScope.launch {
            _currentScreenRouteState.value = route
        }
    }

    fun setUserState(user: User?) {
        viewModelScope.launch {
            _userState.value = user
        }
    }

    fun setTopAppBarState(myTopAppBarState: MyTopAppBarState) {
        viewModelScope.launch {
            _topAppBarState.value = myTopAppBarState
        }
    }

    fun setTopAppBarActions(topBarActions: List<TopAppBarItem>) {
        viewModelScope.launch {
            _topAppBarState.value = _topAppBarState.value.copy(actions = topBarActions)
        }
    }

    fun setTopAppBarMenuActions(topBarMenuActions: List<TopAppBarItem>) {
        viewModelScope.launch {
            _topAppBarState.value = _topAppBarState.value.copy(menuActions = topBarMenuActions)
        }
    }

    fun clearAdvancedFabMenuState() {
        viewModelScope.launch {
            _advancedFabMenuState.value = AdvancedFabMenuState()
        }
    }

    fun setAdvancedFabMenuItems(
        verticalItems: List<AdvancedFabMenuItem> = emptyList(),
        horizontalItems: List<AdvancedFabMenuItem> = emptyList(),
        setNotExpanded: Boolean = true
    ) {
        viewModelScope.launch {
            _advancedFabMenuState.emit(
                if (setNotExpanded) {
                    _advancedFabMenuState.value.copy(
                        expandedOnCreate = false,
                        verticalItems = verticalItems,
                        horizontalItems = horizontalItems
                    )
                } else {
                    _advancedFabMenuState.value.copy(
                        verticalItems = verticalItems,
                        horizontalItems = horizontalItems
                    )
                }
            )
        }
    }

    fun logout(partialLogout: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_userState.value != null) {
                authenticationUseCase.logout(
                    partialLogout = partialLogout,
                    onLogout = {
                        setUserState(null)
                    }
                )
            }
        }
    }

    suspend fun showSnackbar(snackbarVisuals: MySnackbarVisuals): SnackbarResult {
        return _snackbarHostState.value.showSnackbar(snackbarVisuals)
    }

    fun startSetRemindersService(context: Context) {
        Intent(context, SetRemindersService::class.java).also { mIntent ->
            context.startService(mIntent)
        }
    }

    fun launchPeriodicWorkers() {
        workerManager.launchDeleteWorker()
        workerManager.launchSynchronizeWorker()
    }
}