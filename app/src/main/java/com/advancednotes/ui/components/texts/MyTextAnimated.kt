package com.advancednotes.ui.components.texts

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.advancednotes.common.Constants.Companion.ANIMATED_TEXT_DELAY
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MyTextAnimated(
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    var textAnimated by remember { mutableStateOf("") }
    var animationJob by remember { mutableStateOf<Job?>(null) }

    val isAnimationComplete = textAnimated == text

    val clickableModifier: Modifier = if (!isAnimationComplete) {
        Modifier.clickable {
            animationJob?.cancel()
            textAnimated = text

            animationJob = null
        }
    } else Modifier

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

    Text(
        text = textAnimated,
        modifier = modifier.then(clickableModifier),
        color = color ?: Color.Unspecified,
        textAlign = textAlign,
        maxLines = maxLines,
        style = textStyle
    )
}

@Preview(
    name = "light_preview",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun MyTextAnimatedPreviewLight() {
    MyTextAnimated("Preview")
}