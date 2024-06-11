package com.advancednotes.utils.notes

import android.content.Context
import com.advancednotes.R
import com.advancednotes.domain.models.Frequency

object NotesHelper {

    fun getReminderFrequencyText(context: Context, frequency: Frequency): String {
        val text = when (frequency) {
            Frequency.NONE -> context.getString(R.string.reminder_frequency_none)
            Frequency.DAILY -> context.getString(R.string.reminder_frequency_daily)
            Frequency.WEEKLY -> context.getString(R.string.reminder_frequency_weekly)
            Frequency.MONTHLY -> context.getString(R.string.reminder_frequency_monthly)
            Frequency.YEARLY -> context.getString(R.string.reminder_frequency_yearly)
        }

        return text
    }
}