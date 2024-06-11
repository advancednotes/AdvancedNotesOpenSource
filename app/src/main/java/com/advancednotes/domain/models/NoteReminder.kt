package com.advancednotes.domain.models

import androidx.annotation.Keep
import com.advancednotes.utils.date.DateHelper.parseDateToString
import com.advancednotes.utils.date.DateHelper.parseStringToDate
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import java.util.Date

@Keep
data class NoteReminder(
    @SerializedName("reminderDate") val reminderDate: Date,
    @SerializedName("frequency") val frequency: Frequency = Frequency.NONE
)

@Keep
data class NoteReminderDB(
    @SerializedName("reminderDate") val reminderDate: String,
    @SerializedName("frequency") val frequency: String
)

fun parseNoteReminderToDomain(reminder: String?): NoteReminder? {
    if (reminder == null) {
        return null
    }

    val gson: Gson = GsonBuilder().serializeNulls().create()

    val reminderAux = gson.fromJson(reminder, NoteReminderDB::class.java)

    return NoteReminder(
        reminderDate = parseStringToDate(reminderAux.reminderDate),
        frequency = findFrequencyValue(reminderAux.frequency)
    )
}

fun parseNoteReminderToDatabase(reminder: NoteReminder?): String? {
    if (reminder == null) {
        return null
    }

    val gson: Gson = GsonBuilder().serializeNulls().create()

    return gson.toJson(
        NoteReminderDB(
            reminderDate = parseDateToString(reminder.reminderDate),
            frequency = reminder.frequency.value
        )
    )
}

enum class Frequency(val value: String) {
    NONE("none"),
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    YEARLY("yearly"),
}

fun findFrequencyValue(value: String): Frequency {
    return Frequency.values().find { it.value == value } ?: Frequency.NONE
}