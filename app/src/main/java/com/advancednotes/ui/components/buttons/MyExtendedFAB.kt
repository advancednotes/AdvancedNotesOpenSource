package com.advancednotes.ui.components.buttons

import android.content.res.Configuration
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.advancednotes.ui.components.texts.MyText

@Composable
fun MyExtendedFAB(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    expanded: Boolean,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = icon,
        text = text,
        modifier = modifier,
        expanded = expanded,
        shape = RoundedCornerShape(100)
    )
}

@Preview(
    name = "expanded",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun MyExtendedFABPreviewLight() {
    MyExtendedFAB(
        onClick = {

        },
        icon = { Icon(Icons.Filled.Add, null) },
        text = { MyText(text = "Extended FAB") },
        expanded = true
    )
}

@Preview(
    name = "collapsed",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun MyExtendedFABPreview() {
    MyExtendedFAB(
        onClick = {

        },
        icon = { Icon(Icons.Filled.Add, null) },
        text = { MyText(text = "Extended FAB") },
        expanded = false
    )
}