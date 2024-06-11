package com.advancednotes.ui.components.buttons

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.advancednotes.common.Constants.Companion.ANIMATED_TEXT_DELAY
import com.advancednotes.ui.components.texts.MyText
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MyTextButtonAnimated(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    icon: ImageVector? = null,
    colors: ButtonColors = ButtonDefaults.textButtonColors()
) {
    var textAnimated: String by remember { mutableStateOf("") }
    var animationJob: Job? by remember { mutableStateOf(null) }

    val isAnimationComplete = textAnimated == text

    LaunchedEffect(text) {
        textAnimated = ""

        animationJob?.cancel()
        animationJob = launch {
            text.chunked(1).forEachIndexed { index, _ ->
                delay(ANIMATED_TEXT_DELAY)
                textAnimated = text.substring(0, index + 1)
            }
        }
    }

    TextButton(
        onClick = {
            if (!isAnimationComplete) {
                animationJob?.cancel()
                textAnimated = text

                animationJob = null
            }

            onClick()
        },
        modifier = modifier,
        colors = colors
    ) {
        if (icon != null) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        }

        MyText(
            text = textAnimated,
            textStyle = textStyle
        )
    }
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun MyTextButtonAnimatedPreviewLight() {
    MyTextButtonAnimated(
        text = "Preview",
        icon = Icons.Filled.Map,
        onClick = {

        }
    )
}