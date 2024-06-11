package com.advancednotes.ui.components.notes

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.advancednotes.domain.models.ContextMenuItem
import com.advancednotes.domain.models.Note
import com.advancednotes.ui.components.MyDropdownMenu
import com.advancednotes.ui.components.cards.MyElevatedCardStyled
import com.advancednotes.ui.components.chips.ReminderChip
import com.advancednotes.ui.components.texts.MyText
import com.advancednotes.utils.date.DateHelper.dateIsOld
import com.advancednotes.utils.date.DateHelper.getDateRemainingInShortFormat
import com.advancednotes.utils.date.DateHelper.parseDateToString

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    note: Note,
    contextMenuItemData: List<ContextMenuItem>,
    onClick: () -> Unit,
) {
    var noteItemDropDownExpanded by rememberSaveable { mutableStateOf(false) }

    MyElevatedCardStyled {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        onClick.invoke()
                    },
                    onLongClick = {
                        noteItemDropDownExpanded = true
                    }
                )
                .padding(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalAlignment = Alignment.Top
            ) {
                MyText(
                    text = note.name,
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.bodyMedium
                )

                if (note.fixed) {
                    Icon(
                        imageVector = Icons.Filled.PushPin,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            /*if (note.advancedContent != null) {
                AdvancedContent(
                    isLoading = false,
                    advancedContent = note.advancedContent
                )
            }*/

            /*note.content?.let {
                MyText(
                    text = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }*/

            note.reminder?.let {
                val dateIsOld: Boolean = dateIsOld(date = it.reminderDate)

                ReminderChip(
                    text = getDateRemainingInShortFormat(parseDateToString(it.reminderDate)),
                    dateIsOld = dateIsOld,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .align(Alignment.End)
                )
            }

            MyDropdownMenu(
                expanded = noteItemDropDownExpanded,
                onDismissRequest = {
                    noteItemDropDownExpanded = false
                }
            ) {
                contextMenuItemData.forEach { dropdownItem ->
                    DropdownMenuItem(
                        text = { Text(text = dropdownItem.text) },
                        onClick = {
                            dropdownItem.onClick()
                            noteItemDropDownExpanded = false
                        },
                        leadingIcon = {
                            dropdownItem.icon?.let {
                                Icon(
                                    imageVector = it,
                                    contentDescription = null,
                                    modifier = Modifier.size(AssistChipDefaults.IconSize),
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = false
)
@Composable
fun NoteItemPreviewLight() {
    NoteItem(
        note = Note(name = "Preview"),
        contextMenuItemData = listOf(ContextMenuItem("One", {})),
        onClick = {}
    )
}

@Preview(
    name = "night_preview",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = false
)
@Composable
fun NoteItemPreviewNight() {
    NoteItem(
        note = Note(name = "Preview"),
        contextMenuItemData = listOf(ContextMenuItem("One", {})),
        onClick = {}
    )
}