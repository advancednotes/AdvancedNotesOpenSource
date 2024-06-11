package com.advancednotes.utils.colors

import android.graphics.Color
import androidx.compose.ui.graphics.toArgb

object ColorsHelper {
    fun getHexColor(color: androidx.compose.ui.graphics.Color): String {
        return String.format("#%06X", color.toArgb() and 0xFFFFFF)
    }

    fun getAdaptiveTextColor(color: Int): Int {
        return when (isColorTooDark(color)) {
            true -> Color.WHITE
            false -> Color.BLACK
        }
    }

    private fun isColorTooDark(color: Int): Boolean {
        val red = (color shr 16) and 0xFF
        val green = (color shr 8) and 0xFF
        val blue = color and 0xFF

        val brightness = (red * 299 + green * 587 + blue * 114) / 1000

        return brightness <= 128
    }
}