package com.advancednotes.ui.components.single_note.advanced_content

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.advancednotes.R
import com.advancednotes.ui.components.buttons.MyTextButton
import com.advancednotes.ui.components.buttons.MyTextButtonAnimated

@Composable
fun AdvancedContentAddButtons(
    onAddText: () -> Unit,
    onAddList: () -> Unit,
    orientation: AdvancedContentAddButtonsOrientation,
    modifier: Modifier = Modifier
) {
    when (orientation) {
        AdvancedContentAddButtonsOrientation.VERTICAL -> {
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(7.dp),
                horizontalAlignment = Alignment.Start
            ) {
                MyTextButtonAnimated(
                    icon = Icons.Default.TextFields,
                    text = stringResource(id = R.string.advanced_content_add_text),
                    onClick = onAddText
                )

                MyTextButtonAnimated(
                    icon = Icons.AutoMirrored.Filled.List,
                    text = stringResource(id = R.string.advanced_content_add_list),
                    onClick = onAddList
                )
            }
        }

        AdvancedContentAddButtonsOrientation.HORIZONTAL -> {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(7.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MyTextButton(
                    icon = Icons.Default.TextFields,
                    text = stringResource(id = R.string.advanced_content_add_text),
                    onClick = onAddText
                )

                MyTextButton(
                    icon = Icons.AutoMirrored.Filled.List,
                    text = stringResource(id = R.string.advanced_content_add_list),
                    onClick = onAddList
                )
            }
        }
    }
}

enum class AdvancedContentAddButtonsOrientation {
    VERTICAL,
    HORIZONTAL
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun AdvancedContentAddButtonsPreviewLight() {
    AdvancedContentAddButtons(
        onAddText = {},
        onAddList = {},
        orientation = AdvancedContentAddButtonsOrientation.VERTICAL
    )
}