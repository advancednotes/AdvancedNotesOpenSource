package com.advancednotes.domain.models

import androidx.annotation.Keep

@Keep
enum class AdvancedContentType(val value: String) {
    TEXT("text"),
    LIST("list"),
    OTHER("other"),
}