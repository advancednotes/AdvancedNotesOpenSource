package com.advancednotes.ui.screens.single_note.dialogs

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.advancednotes.R
import com.advancednotes.domain.models.Tag
import com.advancednotes.ui.components.ItemSelectable
import com.advancednotes.ui.components.MyAlertDialog
import com.advancednotes.ui.components.texts.MyText

@Composable
fun TagsDialog(
    opened: Boolean,
    tags: List<Tag>,
    noteTagsUUIDs: List<String>,
    onSelectTag: (tagUUID: String) -> Unit,
    onCloseDialog: () -> Unit
) {
    MyAlertDialog(
        opened = opened,
        onDismissRequest = {
            onCloseDialog()
        },
        title = stringResource(id = R.string.screen_tags_label),
        content = {
            if (tags.isNotEmpty()) {
                LazyColumn {
                    items(tags) { tag ->
                        ItemSelectable(
                            text = tag.name,
                            selected = noteTagsUUIDs.any { it == tag.uuid },
                            onClick = {
                                onSelectTag(tag.uuid)
                            }
                        )
                    }
                }
            } else {
                MyText(text = stringResource(id = R.string.content_empty_tags))
            }
        }
    )
}