package app.mybad.notifier.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import app.mybad.utils.DAYS_A_WEEK
import app.mybad.utils.atStartOfDay
import app.mybad.utils.atStartOfDaySystemToUTC
import app.mybad.utils.atStartOfMonth
import app.mybad.utils.currentDateTime
import app.mybad.utils.dayShortDisplay
import app.mybad.utils.minusDays
import app.mybad.utils.plusDays
import kotlinx.datetime.LocalDateTime

@Composable
fun CalendarSelectorScreen(
    modifier: Modifier = Modifier,
    startDay: LocalDateTime,
    endDay: LocalDateTime,
    editStart: Boolean,
    date: LocalDateTime = currentDateTime(),
    onSelect: (date: LocalDateTime?) -> Unit
) {
    var sDate by remember { mutableStateOf(date) }
    var selectedDiapason by remember { mutableStateOf(Pair(startDay, endDay)) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            MonthSelector(
                date = sDate.atStartOfDaySystemToUTC(),
                onSwitch = { sDate = it }
            )
            Spacer(Modifier.height(16.dp))
            CalendarSelector(
                date = sDate,
                startDay = selectedDiapason.first,
                endDay = selectedDiapason.second,
                editStart = editStart,
                onSelect = { selectDate ->
                    selectedDiapason = if (editStart) {
                        selectDate to endDay
                    } else {
                        startDay to selectDate
                    }
                }
            )
        }
        Spacer(Modifier.height(16.dp))
        ReUseFilledButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textId = R.string.settings_save,
            onClick = {
                if (editStart) {
                    onSelect(selectedDiapason.first)
                } else {
                    onSelect(selectedDiapason.second)
                }
            }
        )
    }
}

@Composable
fun SingleDayCalendarSelector(
    modifier: Modifier = Modifier,
    initDay: LocalDateTime,
    date: LocalDateTime = currentDateTime(),
    onSelect: (date: LocalDateTime?) -> Unit
) {
    var sDate by remember { mutableStateOf(date) }
    var selectedDay by remember { mutableStateOf(initDay) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            MonthSelector(
                date = sDate.atStartOfDaySystemToUTC(),
                onSwitch = { sDate = it }
            )
            Spacer(Modifier.height(16.dp))
            SingleDaySelector(
                date = sDate,
                startDay = selectedDay,
                onSelect = { sd ->
                    selectedDay = sd
                }
            )
        }
        Spacer(Modifier.height(16.dp))
        ReUseFilledButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textId = R.string.settings_save,
            onClick = {
                onSelect(selectedDay)
            }
        )
    }
}

@Composable
fun CalendarSelector(
    modifier: Modifier = Modifier,
    date: LocalDateTime,
    startDay: LocalDateTime,
    endDay: LocalDateTime,
    editStart: Boolean,
    onSelect: (date: LocalDateTime) -> Unit = {}
) {
    var startDate by remember { mutableStateOf(startDay) }
    var endDate by remember { mutableStateOf(endDay) }
    val currentDate = currentDateTime()
    val cdr: Array<Array<LocalDateTime?>> = Array(6) {
        Array(DAYS_A_WEEK) { null }
    }
    val fwd = date.atStartOfMonth()
    repeat(6) { w ->
        repeat(DAYS_A_WEEK) { day ->
            if (w == 0 && day < fwd.dayOfWeek.value) {
                cdr[w][day] = fwd.minusDays(fwd.dayOfWeek.ordinal - day)
            } else {
                cdr[w][day] = fwd.plusDays(w * DAYS_A_WEEK + day - fwd.dayOfWeek.ordinal)
            }
        }
    }

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
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
            val currentDateTime = currentDateTime()
            repeat(6) { w ->
                if (cdr[w].any { it?.month == date.month }) {
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
                                date = cdr[w][day],
                                isInRange = (cdr[w][day] ?: currentDate) in startDate..endDate,
                                isOtherMonth = cdr[w][day]?.month != date.month
                            ) {
                                if (editStart) {
                                    startDate = it ?: currentDateTime
                                    if (startDate >= endDate) startDate = endDate
                                } else {
                                    endDate = it ?: currentDateTime
                                    if (endDate <= startDate) endDate = startDate
                                }
                                onSelect(it ?: currentDateTime)
                            }
                        }
                    }
                } else if (w > 4) Spacer(Modifier.height(48.dp))
            }
        }
    }
}

@Composable
private fun SingleDaySelector(
    modifier: Modifier = Modifier,
    date: LocalDateTime,
    startDay: LocalDateTime,
    onSelect: (date: LocalDateTime) -> Unit = {}
) {
    var selectedDate by remember { mutableStateOf(startDay) }
    val cdr: Array<Array<LocalDateTime?>> = Array(6) {
        Array(DAYS_A_WEEK) { null }
    }
    val fwd = date.atStartOfMonth()
    repeat(6) { w ->
        repeat(DAYS_A_WEEK) { day ->
            if (w == 0 && day < fwd.dayOfWeek.value) {
                cdr[w][day] = fwd.minusDays(fwd.dayOfWeek.ordinal - day)
            } else {
                cdr[w][day] = fwd.plusDays(w * DAYS_A_WEEK + day - fwd.dayOfWeek.ordinal)
            }
        }
    }
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
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
                .padding(top = 8.dp)
                .fillMaxWidth()
        )
        val currentDateTime = currentDateTime()
        repeat(6) { w ->
            if (cdr[w].any { it?.month == date.month }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    repeat(DAYS_A_WEEK) { day ->
                        val thisDate = cdr[w][day]
                        CalendarDayItem(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .fillMaxWidth()
                                .weight(1f, false),
                            date = thisDate,
                            isInRange = thisDate?.atStartOfDay() == selectedDate.atStartOfDay(),
                            isOtherMonth = thisDate?.month != date.month
                        ) {
                            selectedDate = it ?: currentDateTime
                            onSelect(selectedDate)
                        }
                    }
                }
            } else if (w > 4) Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
private fun CalendarDayItem(
    modifier: Modifier = Modifier,
    date: LocalDateTime?,
    isInRange: Boolean = false,
    isOtherMonth: Boolean = false,
    onSelect: (LocalDateTime?) -> Unit = {}
) {
    val outlineColor = MaterialTheme.colorScheme.primary

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(40.dp)
            .alpha(if (isOtherMonth) 0.5f else 1f)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onSelect(date)
            }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp),
            shape = CircleShape,
            border = if (isInRange) BorderStroke(1.dp, outlineColor) else null
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = date?.dayOfMonth.toString(),
                    style = Typography.bodyLarge,
                    color = if (isInRange) MaterialTheme.colorScheme.primary else Color.Unspecified
                )
            }
        }
    }
}
