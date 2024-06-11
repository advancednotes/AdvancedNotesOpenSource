package com.advancednotes.ui.components.advanced_fab_menu

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.advancednotes.common.Constants.Companion.HORIZONTAL_PADDING
import com.advancednotes.common.Constants.Companion.VERTICAL_PADDING
import com.advancednotes.ui.components.advanced_fab_menu.components.AdvancedFabMenuButton

@Composable
fun AdvancedFabMenu(
    advancedFabMenuState: AdvancedFabMenuState
) {
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(advancedFabMenuState) {
        if (!advancedFabMenuState.expandedOnCreate) {
            expanded = advancedFabMenuState.expandedOnCreate
        }
    }

    if (
        advancedFabMenuState.verticalItems.isNotEmpty() ||
        advancedFabMenuState.horizontalItems.isNotEmpty()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = HORIZONTAL_PADDING,
                    vertical = VERTICAL_PADDING
                )
        ) {
            AdvancedFabMenuButton(
                icon = if (expanded) {
                    Icons.Default.Close
                } else Icons.Default.Apps,
                onClick = { expanded = !expanded },
                modifier = Modifier.align(Alignment.BottomEnd)
            )

            if (expanded) {
                Column(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .wrapContentHeight(Alignment.Bottom)
                        .offset(y = (-70).dp)
                        .align(Alignment.BottomEnd),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    advancedFabMenuState.verticalItems.forEach { item ->
                        AdvancedFabMenuButton(
                            icon = item.icon,
                            onClick = item.onClick,
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = item.text
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .wrapContentHeight(Alignment.CenterVertically)
                        .offset(x = (-70).dp)
                        .align(Alignment.BottomEnd),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    advancedFabMenuState.horizontalItems.forEach { item ->
                        AdvancedFabMenuButton(
                            icon = item.icon,
                            text = item.text,
                            modifier = Modifier,
                            onClick = item.onClick
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun AdvancedFabMenuPreviewLight() {
    AdvancedFabMenu(
        AdvancedFabMenuState()
    )
}