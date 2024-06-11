package com.advancednotes.ui.components.navigation_drawer

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.advancednotes.R
import com.advancednotes.ui.components.texts.MyText
import com.advancednotes.ui.theme.Green
import com.advancednotes.ui.theme.Red

@Composable
fun SessionStatus(
    isLoggedIn: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Circle,
                contentDescription = null,
                modifier = Modifier.size(7.dp),
                tint = if (isLoggedIn) Green else Red
            )

            Spacer(modifier = Modifier.width(7.dp))

            MyText(
                text = if (isLoggedIn) {
                    stringResource(id = R.string.session_active)
                } else stringResource(id = R.string.session_inactive),
                textStyle = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun SessionStatusPreviewLight() {
    SessionStatus(true)
}

@Preview(
    name = "night_preview",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun SessionStatusPreviewNight() {
    SessionStatus(false)
}