package com.advancednotes.utils.date

import android.annotation.SuppressLint
import android.content.Context
import com.advancednotes.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateHelper {

    fun getCurrentDate(): Date {
        val calendar: Calendar = Calendar.getInstance(Locale.getDefault())
        return calendar.time
    }

    fun getCurrentDateFromSpain(): Date {
        val timeZoneSpain: TimeZone = TimeZone.getTimeZone("Europe/Madrid")

        val calendar: Calendar = Calendar.getInstance(timeZoneSpain)
        return calendar.time
    }

    fun parseDateToString(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(date)
    }

    fun parseStringToDate(dateString: String): Date {
        val calendar: Calendar = Calendar.getInstance(Locale.getDefault())

        val date: String = dateString.split(" ")[0]
        val time: String = dateString.split(" ")[1]

        calendar.set(Calendar.YEAR, date.split("-")[0].toInt())
        calendar.set(Calendar.MONTH, getMonthConstant(date.split("-")[1].toInt()))
        calendar.set(Calendar.DAY_OF_MONTH, date.split("-")[2].toInt())

        calendar.set(Calendar.HOUR_OF_DAY, time.split(":")[0].toInt())
        calendar.set(Calendar.MINUTE, time.split(":")[1].toInt())
        calendar.set(Calendar.SECOND, time.split(":")[2].toInt())

        return calendar.time
    }

    private fun getMonthConstant(month: Int): Int {
        return when (month) {
            1 -> Calendar.JANUARY
            2 -> Calendar.FEBRUARY
            3 -> Calendar.MARCH
            4 -> Calendar.APRIL
            5 -> Calendar.MAY
            6 -> Calendar.JUNE
            7 -> Calendar.JULY
            8 -> Calendar.AUGUST
            9 -> Calendar.SEPTEMBER
            10 -> Calendar.OCTOBER
            11 -> Calendar.NOVEMBER
            12 -> Calendar.DECEMBER

            else -> Calendar.JANUARY
        }
    }

    fun getDateRemainingInShortFormat(noteReminderDate: String): String {
        val date: Date = parseStringToDate(noteReminderDate)
        val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
        return sdf.format(date)
    }

    fun parseDatePickerStateToString(millis: Long): String {
        val calendar: Calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = millis
        return parseDateToString(calendar.time).split(" ")[0]
    }

    fun parseTimePickerStateToString(hour: Int, minute: Int): String {
        val formattedHour = hour.toString().padStart(2, '0')
        val formattedMinute = minute.toString().padStart(2, '0')
        return "$formattedHour:$formattedMinute:00"
    }

    fun parseReminderDateToString(date: String, time: String): String {
        return "$date $time"
    }

    fun getReminderDateInNaturalLanguage(context: Context, reminderDate: Date): String {
        val calendar: Calendar = Calendar.getInstance(Locale.getDefault())
        calendar.time = reminderDate

        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val month: String? =
            calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val year: Int = calendar.get(Calendar.YEAR)
        val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minute: Int = calendar.get(Calendar.MINUTE)

        return context.getString(
            R.string.natural_lang_formatted_date,
            day,
            month,
            year,
            "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
        )
    }

    fun dateIsOld(date: Date): Boolean {
        return getCurrentDate() >= date
    }

    fun addOneDayToDate(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return calendar.time
    }

    fun addOneWeekToDate(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.WEEK_OF_YEAR, 1)
        return calendar.time
    }

    fun addOneMonthToDate(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MONTH, 1)
        return calendar.time
    }

    fun addOneYearToDate(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.YEAR, 1)
        return calendar.time
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateForFileName(): String {
        val sdf = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        return sdf.format(getCurrentDate())
    }
}