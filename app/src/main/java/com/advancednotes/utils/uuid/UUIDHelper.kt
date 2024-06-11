package com.advancednotes.utils.uuid

import java.util.UUID

object UUIDHelper {

    fun getRandomUUID(): String = UUID.randomUUID().toString()

    fun generateNotificationUUID(): Int {
        val uuid = getRandomUUID()
        val numericOnly = uuid.filter { it.isDigit() }.toIntOrNull()

        if (numericOnly != null) {
            return numericOnly
        }

        return System.currentTimeMillis().toInt() + (0..100).random()
    }
}