package app.mybad.notifier.ui.common

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.theme.R
import app.mybad.utils.DAYS_A_WEEK
import app.mybad.utils.LIMIT_END_MAX
import app.mybad.utils.LIMIT_START_MAX
import app.mybad.utils.LIMIT_START_MIN
import app.mybad.utils.WEEKS_PER_MONTH
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atStartOfDay
import app.mybad.utils.currentDateTimeSystem
import app.mybad.utils.dayShortDisplay
import app.mybad.utils.displayDateFull
import app.mybad.utils.displayDateTime
import app.mybad.utils.displayDay
import app.mybad.utils.firstDayOfMonth
import app.mybad.utils.initWeekAndDayOfMonth
import app.mybad.utils.isBetweenDay
import app.mybad.utils.isEqualsDay
import app.mybad.utils.isEqualsMonth
import app.mybad.utils.isNotEqualsMonth
import app.mybad.utils.lastDayOfMonth
import app.mybad.utils.minusMonths
import app.mybad.utils.plusMonths
import kotlinx.datetime.LocalDateTime

@Composable
fun ParameterIndicatorCalendarSelector(
    @StringRes parameter: Int,
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    editStart: Boolean,
    onSelect: (Pair<LocalDateTime, LocalDateTime>) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    ParameterIndicator(
        name = parameter,
        value = (if (editStart) startDate else endDate).displayDateFull(),
        onClick = { expanded = true }
    )
    ReUseAnimatedVisibility(visible = expanded) {
        CalendarSelectorDialog(
            startDate = startDate,
            endDate = endDate,
            editStart = editStart,
            onDismiss = { expanded = false },
            onDateSelected = onSelect::invoke,
        )
    }
}

@Composable
private fun CalendarSelectorDialog(
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    editStart: Boolean,
    onDismiss: () -> Unit = {},
    onDateSelected: (Pair<LocalDateTime, LocalDateTime>) -> Unit = {},
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
            CalendarPairSelectorView(
                editStart = editStart,
                startDate = startDate,
                endDate = endDate,
                dateOfMonth = if (editStart) startDate else endDate,// начальный отображаемый месяц
                onSelect = {
                    onDismiss()
                    onDateSelected(it)
                }
            )
        }
    }
}

@Composable
private fun CalendarPairSelectorView(
    modifier: Modifier = Modifier,
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    editStart: Boolean,
    dateOfMonth: LocalDateTime = currentDateTimeSystem(),
    onSelect: (Pair<LocalDateTime, LocalDateTime>) -> Unit
) {
    val currentDate by remember { mutableStateOf(currentDateTimeSystem()) }
    val minStartDate by remember {
        mutableStateOf(currentDate.minusMonths(LIMIT_START_MIN).firstDayOfMonth())
    }
    val maxStartDate by remember {
        mutableStateOf(currentDate.plusMonths(LIMIT_START_MAX).lastDayOfMonth())
    }
    val maxEndDate by remember {
        mutableStateOf(currentDate.plusMonths(LIMIT_END_MAX).lastDayOfMonth())
    }
    var startDay by remember { mutableStateOf(startDate) }
    var endDay by remember { mutableStateOf(endDate) }

    var sDate by remember { mutableStateOf(dateOfMonth) }
    // начало месяца и от этого дня берется неделя и первый день недели
    var datesWeeks by remember {
        Log.w("VTTAG", "CalendarPairSelectorView::init: date=${sDate.displayDateTime()}")
        mutableStateOf(initWeekAndDayOfMonth(sDate))
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            MonthSelector(
                date = sDate,
                onSwitch = {
                    sDate = it
                    datesWeeks = initWeekAndDayOfMonth(sDate)
                    Log.w(
                        "VTTAG",
                        "CalendarPairSelectorView::update: date=${sDate.displayDateTime()}"
                    )
                }
            )
            Spacer(Modifier.height(16.dp))
            CalendarPairSelector(
                date = sDate,
                startDate = startDay,
                endDate = endDay,
                editStart = editStart,
                datesWeeks = datesWeeks,
                onSelect = {
                    if (editStart) {
                        startDay = when {
                            it < minStartDate -> minStartDate
                            it > maxStartDate -> maxStartDate
                            else -> it
                        }.atStartOfDay()
                        // тут нужно менять дату иначе не получиться выбрать дату после окончания курса
                        if (startDay > endDay) {
                            endDay = startDay.atEndOfDay()
                        } else if (endDay > endDate) {
                            endDay = if (startDay < endDate) endDate else startDay.atEndOfDay()
                        }
                    } else {
                        endDay = when {
                            it > maxEndDate -> maxEndDate
                            it <= startDay -> startDay
                            else -> it
                        }.atEndOfDay()
                    }
                    Log.w(
                        "VTTAG", "CalendarPairSelectorView::onSelect: day=${
                            if (editStart) startDay.dayOfMonth else endDay.dayOfMonth
                        } dayOfMonth=${
                            if (editStart) startDay.displayDay() else endDay.displayDay()
                        } startDay=${
                            startDay.displayDateTime()
                        } endDay=${endDay.displayDateTime()} endDate=${
                            endDate.displayDateTime()
                        }"
                    )
                }
            )
        }
        Spacer(Modifier.height(16.dp))
        ReUseFilledButton(
            textId = R.string.settings_save,
            onClick = {
                onSelect(startDay to endDay)
            }
        )
    }
}

@Composable
private fun CalendarPairSelector(
    modifier: Modifier = Modifier,
    date: LocalDateTime,
    editStart: Boolean,
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    datesWeeks: Array<Array<LocalDateTime>>,
    onSelect: (LocalDateTime) -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.5f)
            ) {
                repeat(DAYS_A_WEEK) { day ->
                    Text(
                        text = day.dayShortDisplay(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, false)
                    )
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )
            if (datesWeeks.size == WEEKS_PER_MONTH && datesWeeks.all { it.size == DAYS_A_WEEK }) {
                repeat(WEEKS_PER_MONTH) { week ->
                    if (datesWeeks[week].any { it.isEqualsMonth(date) }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            repeat(DAYS_A_WEEK) { day ->
                                CalendarDayItem(
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                        .fillMaxWidth()
                                        .weight(1f, false),
                                    initDate = datesWeeks[week][day].isEqualsDay(
                                        if (editStart) startDate else endDate
                                    ),
                                    date = datesWeeks[week][day],
                                    inRange = datesWeeks[week][day].isBetweenDay(
                                        startDate,
                                        endDate
                                    ),
                                    otherMonth = datesWeeks[week][day].isNotEqualsMonth(date),
                                    onSelect = onSelect::invoke,
                                )
                            }
                        }
                    } else if (week > 4) Spacer(Modifier.height(48.dp))
                }
            }
        }
    }
}

@Composable
private fun CalendarDayItem(
    modifier: Modifier = Modifier,
    date: LocalDateTime,
    initDate: Boolean,
    inRange: Boolean = false, // в диапазоне
    otherMonth: Boolean = false,
    onSelect: (LocalDateTime) -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(40.dp)
            .padding(2.dp)
            .border(
                1.dp,
                if (inRange) MaterialTheme.colorScheme.primary else Color.Transparent,
                CircleShape
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { onSelect(date) },
            )
    ) {
        Text(
            modifier = Modifier.alpha(if (otherMonth) 0.5f else 1f),
            text = date.displayDay(),
            style = if (initDate) MaterialTheme.typography.bodyLarge.copy(
                fontSize = MaterialTheme.typography.bodyLarge.fontSize.times(
                    1.2f
                )
            ) else MaterialTheme.typography.bodyLarge,
            color = if (inRange) MaterialTheme.colorScheme.primary else Color.Unspecified
        )
    }
}

@Preview
@Composable
private fun CalendarSelectorDialogPreview() {
    MyBADTheme {
        val startDate = LocalDateTime(2023, 5, 4, 5, 5, 5)
        val endDate = LocalDateTime(2023, 5, 10, 5, 5, 5)
        CalendarSelectorDialog(
            startDate = startDate,
            endDate = endDate,
            editStart = true,
        )
    }
}
