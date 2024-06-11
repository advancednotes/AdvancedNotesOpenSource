package com.advancednotes.ui.screens.single_tag

import android.content.Context
import android.graphics.Color.parseColor
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.advancednotes.R
import com.advancednotes.common.Constants.Companion.CONTENT_PADDING_DEFAULT
import com.advancednotes.common.Constants.Companion.EMPTY_UUID
import com.advancednotes.domain.models.MySnackbarVisuals
import com.advancednotes.domain.models.TopAppBarItem
import com.advancednotes.ui.components.ColorItem
import com.advancednotes.ui.components.ScreenContainer
import com.advancednotes.ui.components.single_tag.SingleTagNoteItem
import com.advancednotes.ui.components.textfields.EmptyTextField
import com.advancednotes.ui.components.texts.MyText
import com.advancednotes.ui.components.top_app_bar.MyTopAppBarState
import com.advancednotes.ui.screens.Screen
import com.advancednotes.ui.screens.main.MainActivityViewModel
import com.advancednotes.ui.screens.single_tag.dialogs.DeleteTagPermanentlyDialog
import com.advancednotes.utils.colors.ColorsHelper.getAdaptiveTextColor
import com.advancednotes.utils.navigation.myNavigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@Composable
fun SingleTagScreen(
    mainScope: CoroutineScope,
    navController: NavController,
    tagUUID: String = EMPTY_UUID,
    mainActivityViewModel: MainActivityViewModel,
    singleTagViewModel: SingleTagViewModel = hiltViewModel()
) {
    val context: Context = LocalContext.current

    val tagState: SingleTagState by singleTagViewModel.singleTagState.collectAsState()

    var tagName: TextFieldValue by remember { mutableStateOf(TextFieldValue(text = "")) }

    var showTagNameEmptyError: Boolean by remember { mutableStateOf(false) }

    var deleteTagDialogOpened: Boolean by remember { mutableStateOf(false) }

    fun onBack() {
        if (singleTagViewModel.isNewTagWithEmptyData()) {
            singleTagViewModel.exit()
            return
        }

        if (!singleTagViewModel.validateData()) {
            if (tagName.text.isEmpty()) {
                showTagNameEmptyError = true
                singleTagViewModel.showSnackbarDiscardChanges()
            }
            return
        }

        mainActivityViewModel.clearAdvancedFabMenuState()
        singleTagViewModel.saveTagAndExit()
    }

    BackHandler {
        onBack()
    }

    LaunchedEffect(Unit) {
        mainScope.launch {
            mainActivityViewModel.setCurrentRouteState(Screen.SingleTagScreen.route)
            mainActivityViewModel.setTopAppBarState(
                MyTopAppBarState(
                    label = context.getString(Screen.SingleTagScreen.labelResource),
                    navigationBack = true,
                    navigationBackAction = {
                        onBack()
                    }
                )
            )
        }

        singleTagViewModel.getTag(tagUUID)
    }

    LaunchedEffect(tagState.isLoading) {
        if (!tagState.isLoading) {
            mainScope.launch {
                if (!singleTagViewModel.isNewTag() && tagState.notesCount == 0) {
                    val topBarActions: MutableList<TopAppBarItem> = mutableListOf()
                    topBarActions.add(
                        TopAppBarItem(
                            icon = Icons.Filled.Delete,
                            text = context.getString(R.string.delete),
                            onClick = {
                                deleteTagDialogOpened = true
                            }
                        )
                    )

                    mainActivityViewModel.setTopAppBarActions(topBarActions)
                }
            }

            tagName = TextFieldValue(text = tagState.tag.name)
        }
    }

    LaunchedEffect(singleTagViewModel.snackbarDiscardChannel) {
        singleTagViewModel.snackbarDiscardChannel.receiveAsFlow().collectLatest {
            val message: String = context.getString(R.string.error_note_tag_name_empty)

            val result: SnackbarResult = mainActivityViewModel.showSnackbar(
                MySnackbarVisuals(
                    message = message,
                    isError = true,
                    actionLabel = context.getString(R.string.discard_changes)
                )
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    singleTagViewModel.exit()
                }

                SnackbarResult.Dismissed -> {}
            }
        }
    }

    LaunchedEffect(singleTagViewModel.exitChannel) {
        singleTagViewModel.exitChannel.receiveAsFlow().collectLatest {
            navController.popBackStack()
        }
    }

    ScreenContainer {
        Column(
            verticalArrangement = Arrangement.spacedBy(CONTENT_PADDING_DEFAULT)
        ) {
            EmptyTextField(
                value = tagName,
                onValueChange = {
                    tagName = it
                    singleTagViewModel.changeTagName(it.text)
                    if (it.text.isNotEmpty()) showTagNameEmptyError = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                textFieldModifier = Modifier
                    .padding(all = 5.dp),
                textStyle = MaterialTheme.typography.titleMedium,
                placeholder = stringResource(id = R.string.tag_name),
                supportingText = if (showTagNameEmptyError && !tagState.isLoading) {
                    context.getString(R.string.error_note_tag_name_empty)
                } else "",
                isError = showTagNameEmptyError && !tagState.isLoading,
                singleLine = true
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                items(tagState.colors) { colorHex ->
                    ColorItem(
                        color = Color(parseColor(colorHex)),
                        iconColor = Color(getAdaptiveTextColor(parseColor(colorHex))),
                        selected = tagState.tag.colorHex == colorHex,
                        onClick = {
                            singleTagViewModel.changeTagColor(colorHex)
                        }
                    )
                }
            }

            Spacer(
                modifier = Modifier.weight(1f)
            )

            if (tagState.notesInTag.isNotEmpty()) {
                MyText(
                    text = stringResource(id = R.string.notes_in_tag_label),
                    textStyle = MaterialTheme.typography.labelMedium
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(tagState.notesInTag) { note ->
                        SingleTagNoteItem(
                            note = note,
                            onClick = {
                                navController.myNavigate(
                                    route = Screen.SingleNoteScreen.route + "?noteUUID=${note.uuid}",
                                    afterNavigate = { mainActivityViewModel.clearAdvancedFabMenuState() }
                                )
                            }
                        )
                    }
                }
            }

            if (tagState.notesArchivedInTag.isNotEmpty()) {
                MyText(
                    text = stringResource(id = R.string.notes_archived_in_tag_label),
                    textStyle = MaterialTheme.typography.labelMedium
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(tagState.notesArchivedInTag) { note ->
                        SingleTagNoteItem(
                            note = note,
                            onClick = {
                                navController.myNavigate(
                                    route = Screen.SingleNoteScreen.route + "?noteUUID=${note.uuid}",
                                    afterNavigate = { mainActivityViewModel.clearAdvancedFabMenuState() }
                                )
                            }
                        )
                    }
                }
            }

            if (tagState.notesTrashedInTag.isNotEmpty()) {
                MyText(
                    text = stringResource(id = R.string.notes_trashed_in_tag_label),
                    textStyle = MaterialTheme.typography.labelMedium
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(tagState.notesTrashedInTag) { note ->
                        SingleTagNoteItem(
                            note = note,
                            onClick = {
                                navController.myNavigate(
                                    route = Screen.TrashedNotesScreen.route,
                                    afterNavigate = { mainActivityViewModel.clearAdvancedFabMenuState() }
                                )
                            }
                        )
                    }
                }
            }
        }

        DeleteTagPermanentlyDialog(
            deleteTagDialogOpened = deleteTagDialogOpened,
            tagName = tagState.tag.name,
            onConfirm = {
                singleTagViewModel.deleteTagPermanently(tagUUID = tagState.tag.uuid)
            },
            onCloseDialog = {
                deleteTagDialogOpened = false
            }
        )
    }
}