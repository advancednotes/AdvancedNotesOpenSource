package com.advancednotes.domain.models

import androidx.annotation.Keep

@Keep
enum class AutoDeleteTrashMode(val value: String) {
    NEVER("never"),
    MONTHLY("monthly"),
    SIX_MONTHLY("six_monthly")
}