package com.advancednotes.ui.components.bottom_app_bar

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.advancednotes.R
import com.advancednotes.domain.models.BottomAppBarItem
import com.advancednotes.ui.components.texts.MyText

@Composable
fun MyBottomAppBar(
    myBottomAppBarState: MyBottomAppBarState
) {
    val hasActions: Boolean = myBottomAppBarState.actions.isNotEmpty()
    val hasFAB: Boolean = myBottomAppBarState.floatingActionButton != null

    val showBottomAppBar: Boolean = myBottomAppBarState.bottomAppBarVisible

    if (showBottomAppBar) {
        BottomAppBar(
            actions = {
                myBottomAppBarState.actions.forEach { item ->
                    IconButton(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        onClick = {
                            item.onClick()
                        }
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier
                                    .size(AssistChipDefaults.IconSize),
                                tint = item.iconTint ?: LocalContentColor.current
                            )
                            MyText(
                                modifier = Modifier
                                    .padding(top = 6.dp),
                                text = item.label,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                textStyle = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            floatingActionButton = myBottomAppBarState.floatingActionButton,
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 0.dp,
                end = 16.dp,
                bottom = 0.dp
            )
        )
    }
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun MyBottomAppBarPreviewLight() {
    MyBottomAppBar(
        MyBottomAppBarState(
            actions = listOf(
                BottomAppBarItem(
                    icon = Icons.Default.Tag,
                    onClick = {
                    },
                    label = stringResource(id = R.string.screen_tags_label)
                ),
                BottomAppBarItem(
                    icon = Icons.Default.LocationCity,
                    onClick = {
                    },
                    label = stringResource(id = R.string.note_location)
                ),
                BottomAppBarItem(
                    icon = Icons.Default.RecordVoiceOver,
                    onClick = {
                    },
                    label = stringResource(id = R.string.note_record)
                )
            )
        )
    )
}