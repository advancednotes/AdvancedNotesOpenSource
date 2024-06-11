package com.advancednotes.ui.components.buttons

import android.content.res.Configuration
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MyFAB(
    onClick: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(100)
    ) {
        content()
    }
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun MyFABPreviewLight() {
    MyFAB(
        onClick = {

        },
        content = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "contentDescription"
            )
        }
    )
}

@Preview(
    name = "night_preview",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun MyFABPreviewNight() {
    MyFAB(
        onClick = {

        },
        content = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "contentDescription"
            )
        }
    )
}