package com.advancednotes.ui.screens.single_note.dialogs.reminder

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.advancednotes.R
import com.advancednotes.domain.models.Frequency
import com.advancednotes.domain.models.NoteReminder
import com.advancednotes.ui.components.MyAlertDialog
import com.advancednotes.ui.components.buttons.MyTextButton
import com.advancednotes.ui.components.single_note.reminder.ReminderField
import com.advancednotes.ui.components.texts.MyText
import com.advancednotes.utils.date.DateHelper.getCurrentDate
import com.advancednotes.utils.date.DateHelper.getReminderDateInNaturalLanguage
import com.advancednotes.utils.date.DateHelper.parseDatePickerStateToString
import com.advancednotes.utils.date.DateHelper.parseReminderDateToString
import com.advancednotes.utils.date.DateHelper.parseStringToDate
import com.advancednotes.utils.date.DateHelper.parseTimePickerStateToString
import com.advancednotes.utils.notes.NotesHelper.getReminderFrequencyText
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderDialog(
    opened: Boolean,
    reminder: NoteReminder?,
    onChangeReminder: (reminder: NoteReminder?) -> Unit,
    onCloseDialog: () -> Unit
) {
    val context: Context = LocalContext.current

    var datePickerDialogOpened: Boolean by remember { mutableStateOf(false) }
    var timePickerDialogOpened: Boolean by remember { mutableStateOf(false) }
    var frequencyDialogOpened: Boolean by remember { mutableStateOf(false) }

    var dateMillis: Long? by remember { mutableStateOf(null) }
    var hour: Int? by remember { mutableStateOf(null) }
    var minute: Int? by remember { mutableStateOf(null) }

    var frequency: Frequency by remember { mutableStateOf(Frequency.NONE) }

    var reminderAux: NoteReminder? by remember { mutableStateOf(null) }

    var dateText: String by remember { mutableStateOf("") }
    var timeText: String by remember { mutableStateOf("") }
    var frequencyText: String by remember { mutableStateOf("") }

    fun changeReminderDate(dateMillisAux: Long? = null) {
        dateMillis = dateMillisAux

        dateText = if (dateMillisAux != null) {
            "${context.getString(R.string.date)}: ${
                parseDatePickerStateToString(dateMillisAux)
            }"
        } else context.getString(R.string.date)
    }

    fun changeReminderTime(hourAux: Int? = null, minuteAux: Int? = null) {
        hour = hourAux
        minute = minuteAux

        timeText =
            if (hourAux != null && minuteAux != null) {
                "${context.getString(R.string.time)}: ${
                    parseTimePickerStateToString(hourAux, minuteAux)
                }"
            } else context.getString(R.string.time)
    }

    fun changeReminderFrequency(frequencyAux: Frequency) {
        frequency = frequencyAux

        frequencyText = getReminderFrequencyText(
            context = context,
            frequency = frequencyAux
        )
    }

    LaunchedEffect(reminder) {
        if (reminder != null) {
            val calendar: Calendar = Calendar.getInstance().apply { time = reminder.reminderDate }
            val dateMillisAux: Long = calendar.time.time
            val hourAux: Int = calendar.get(Calendar.HOUR_OF_DAY)
            val minuteAux: Int = calendar.get(Calendar.MINUTE)

            changeReminderDate(dateMillisAux)
            changeReminderTime(hourAux, minuteAux)
            changeReminderFrequency(reminder.frequency)
        } else {
            val calendar: Calendar = Calendar.getInstance().apply { time = getCurrentDate() }
            val dateMillisAux: Long = calendar.time.time

            changeReminderDate(dateMillisAux)
            changeReminderTime(null, null)
            changeReminderFrequency(Frequency.NONE)
        }
    }

    LaunchedEffect(dateMillis, hour, minute, frequency) {
        val reminderDateIsNotNull: Boolean = dateMillis != null && hour != null && minute != null

        reminderAux =
            if (reminderDateIsNotNull) {
                reminderAux?.copy(
                    reminderDate = parseStringToDate(
                        parseReminderDateToString(
                            date = parseDatePickerStateToString(dateMillis!!),
                            time = parseTimePickerStateToString(hour!!, minute!!)
                        )
                    ),
                    frequency = frequency
                ) ?: NoteReminder(
                    reminderDate = parseStringToDate(
                        parseReminderDateToString(
                            date = parseDatePickerStateToString(dateMillis!!),
                            time = parseTimePickerStateToString(hour!!, minute!!)
                        )
                    ),
                    frequency = frequency
                )
            } else null
    }

    MyAlertDialog(
        opened = opened,
        onDismissRequest = {
            onCloseDialog()
        },
        title = stringResource(id = R.string.reminder),
        content = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ReminderField(
                    icon = Icons.Filled.CalendarMonth,
                    text = dateText,
                    onClick = {
                        datePickerDialogOpened = true
                    },
                    modifier = Modifier,
                )

                ReminderField(
                    icon = Icons.Filled.Schedule,
                    text = timeText,
                    onClick = {
                        timePickerDialogOpened = true
                    }
                )

                ReminderField(
                    icon = Icons.Filled.Recycling,
                    text = frequencyText,
                    onClick = {
                        frequencyDialogOpened = true
                    }
                )

                if (reminder != null) {
                    MyTextButton(
                        modifier = Modifier
                            .align(Alignment.End),
                        text = stringResource(id = R.string.clear_button),
                        onClick = {
                            onChangeReminder(null)
                            onCloseDialog()
                        }
                    )
                }

                Column(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (reminderAux != null) {
                        MyText(text = stringResource(id = R.string.note_reminder_date_label))
                        MyText(
                            text = getReminderDateInNaturalLanguage(
                                context = context,
                                reminderDate = reminderAux!!.reminderDate
                            )
                        )
                    } else {
                        MyText(text = stringResource(id = R.string.no_reminder_set))
                    }
                }
            }
        },
        confirmButton = {
            MyTextButton(
                text = stringResource(id = R.string.continue_button),
                onClick = {
                    if (reminderAux != null) {
                        onChangeReminder(reminderAux)
                    }

                    onCloseDialog()
                }
            )
        },
        dismissButton = {
            MyTextButton(
                text = stringResource(id = R.string.cancel_button),
                onClick = {
                    onCloseDialog()
                }
            )
        }
    )

    if (datePickerDialogOpened) {
        DatePickerDialog(
            onDismissRequest = {
                datePickerDialogOpened = false
            },
            content = {
                val datePickerState: DatePickerState = if (dateMillis != null) {
                    rememberDatePickerState(initialSelectedDateMillis = dateMillis)
                } else rememberDatePickerState()

                LaunchedEffect(datePickerState.selectedDateMillis) {
                    changeReminderDate(datePickerState.selectedDateMillis)
                }

                DatePicker(state = datePickerState)
            },
            confirmButton = {
                MyTextButton(
                    text = stringResource(id = R.string.continue_button),
                    onClick = {
                        datePickerDialogOpened = false
                    }
                )
            }
        )
    }

    MyAlertDialog(
        opened = timePickerDialogOpened,
        onDismissRequest = {
            timePickerDialogOpened = false
        },
        title = stringResource(id = R.string.time),
        content = {
            val timePickerState: TimePickerState = if (hour != null && minute != null) {
                rememberTimePickerState(initialHour = hour!!, initialMinute = minute!!)
            } else rememberTimePickerState()

            LaunchedEffect(timePickerState.hour, timePickerState.minute) {
                changeReminderTime(timePickerState.hour, timePickerState.minute)
            }

            TimePicker(state = timePickerState)
        },
        confirmButton = {
            MyTextButton(
                text = stringResource(id = R.string.continue_button),
                onClick = {
                    timePickerDialogOpened = false
                }
            )
        }
    )

    if (frequencyDialogOpened) {
        FrequencyDialog(
            opened = frequencyDialogOpened,
            frequency = reminderAux?.frequency ?: Frequency.NONE,
            onChangeFrequency = {
                changeReminderFrequency(it)
            },
            onCloseDialog = {
                frequencyDialogOpened = false
            }
        )
    }
}