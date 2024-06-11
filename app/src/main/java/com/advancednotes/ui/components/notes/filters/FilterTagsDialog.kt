package com.advancednotes.ui.components.notes.filters

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.advancednotes.R
import com.advancednotes.domain.models.Tag
import com.advancednotes.ui.components.ItemSelectable
import com.advancednotes.ui.components.MyAlertDialog

@Composable
fun FilterTagsDialog(
    opened: Boolean,
    onCloseDialog: () -> Unit,
    usedTags: List<Tag>,
    selectedTagsUUIDs: List<String>,
    onClickTag: (tag: Tag) -> Unit,
) {
    MyAlertDialog(
        opened = opened,
        onDismissRequest = {
            onCloseDialog()
        },
        title = stringResource(id = R.string.filter_by_tags),
        content = {
            LazyColumn {
                items(usedTags) { tag ->
                    ItemSelectable(
                        text = tag.name,
                        selected = selectedTagsUUIDs.any { it == tag.uuid },
                        onClick = {
                            onClickTag(tag)
                        }
                    )
                }
            }
        }
    )
}