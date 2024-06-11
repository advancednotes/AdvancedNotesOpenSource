package com.advancednotes.ui.components.single_note.records

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.advancednotes.R
import com.advancednotes.domain.models.ContextMenuItem
import com.advancednotes.domain.models.NoteFile
import com.advancednotes.ui.screens.single_note.RecordsViewModel
import com.advancednotes.ui.screens.single_note.SingleNoteViewModel
import com.advancednotes.ui.screens.single_note.dialogs.DeleteRecordDialog
import com.advancednotes.ui.screens.single_note.dialogs.DeleteRecordDialogState
import com.advancednotes.ui.screens.single_note.dialogs.RenameRecordDialog
import com.advancednotes.ui.screens.single_note.dialogs.RenameRecordDialogState

@Composable
fun NoteRecords(
    context: Context,
    noteRecords: List<NoteFile>,
    storagePermissionGranted: Boolean,
    openStoragePermissionRationaleDialog: () -> Unit,
    singleNoteViewModel: SingleNoteViewModel,
    recordsViewModel: RecordsViewModel
) {
    var renameRecordDialogState: RenameRecordDialogState by remember {
        mutableStateOf(
            RenameRecordDialogState()
        )
    }
    var deleteRecordDialogState: DeleteRecordDialogState by remember {
        mutableStateOf(
            DeleteRecordDialogState()
        )
    }

    LazyColumn(
        modifier = Modifier.heightIn(0.dp, 300.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(noteRecords) { record ->
            Record(
                record = record,
                contextMenuItemData = listOf(
                    ContextMenuItem(
                        text = stringResource(id = R.string.rename),
                        onClick = {
                            renameRecordDialogState = renameRecordDialogState.copy(
                                opened = true,
                                noteRecord = record
                            )
                        },
                        icon = Icons.Default.Edit
                    ),
                    ContextMenuItem(
                        text = stringResource(id = R.string.download),
                        onClick = {
                            if (storagePermissionGranted) {
                                singleNoteViewModel.downloadRecord(context, record)
                            } else {
                                openStoragePermissionRationaleDialog()
                            }
                        },
                        icon = Icons.Default.Download
                    ),
                    ContextMenuItem(
                        text = stringResource(id = R.string.delete),
                        onClick = {
                            deleteRecordDialogState = deleteRecordDialogState.copy(
                                opened = true,
                                noteRecord = record
                            )
                        },
                        icon = Icons.Default.Delete
                    )
                ),
                recordsViewModel = recordsViewModel
            )
        }
    }

    RenameRecordDialog(
        renameRecordDialogState = renameRecordDialogState,
        onConfirm = { newRecordName ->
            if (newRecordName.isNotEmpty()) {
                singleNoteViewModel.renameRecord(
                    noteRecord = renameRecordDialogState.noteRecord!!,
                    newFileName = newRecordName
                )
            }
        },
        onCloseDialog = {
            renameRecordDialogState =
                renameRecordDialogState.copy(
                    opened = false,
                    noteRecord = null
                )
        }
    )

    DeleteRecordDialog(
        deleteRecordDialogState = deleteRecordDialogState,
        onConfirm = {
            singleNoteViewModel.deleteRecord(deleteRecordDialogState.noteRecord)
        },
        onCloseDialog = {
            deleteRecordDialogState = deleteRecordDialogState.copy(opened = false)
        }
    )
}