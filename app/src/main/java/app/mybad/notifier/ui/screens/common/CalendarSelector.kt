package app.mybad.notifier.ui.screens.common

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
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.mybad.notifier.R
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.utils.atStartOfDaySystemToUTC
import app.mybad.notifier.utils.atStartOfMonth
import app.mybad.notifier.utils.getCurrentDateTime
import app.mybad.notifier.utils.minusDays
import app.mybad.notifier.utils.plusDays
import kotlinx.datetime.LocalDateTime

@Composable
fun CalendarSelectorScreen(
    modifier: Modifier = Modifier,
    date: LocalDateTime = getCurrentDateTime(),
    startDay: LocalDateTime,
    endDay: LocalDateTime,
    onSelect: (date: LocalDateTime?) -> Unit,
    onDismiss: () -> Unit,
    editStart: Boolean,
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
                onSelect = { sd ->
                    selectedDiapason = if (editStart) {
                        sd to endDay
                    } else {
                        startDay to sd
                    }
                }
            )
        }
        Spacer(Modifier.height(16.dp))
        NavigationRow(
            backLabel = stringResource(R.string.settings_cancel),
            nextLabel = stringResource(R.string.settings_save),
            onBack = onDismiss::invoke,
            onNext = {
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
fun CalendarSelector(
    modifier: Modifier = Modifier,
    date: LocalDateTime,
    startDay: LocalDateTime,
    endDay: LocalDateTime,
    editStart: Boolean,
    onSelect: (date: LocalDateTime) -> Unit = { }
) {
    var startDate by remember { mutableStateOf(startDay) }
    var endDate by remember { mutableStateOf(endDay) }
    val days = stringArrayResource(R.array.days_short)
    val currentDate = getCurrentDateTime()
    val cdr: Array<Array<LocalDateTime?>> = Array(6) {
        Array(7) { null }
    }
    val fwd = date.atStartOfMonth()
    for (w in 0..5) {
        for (d in 0..6) {
            if (w == 0 && d < fwd.dayOfWeek.value) {
                cdr[w][d] = fwd.minusDays(fwd.dayOfWeek.ordinal - d)
            } else {
                cdr[w][d] = fwd.plusDays(w * 7 + d - fwd.dayOfWeek.ordinal)
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
                repeat(7) {
                    Text(
                        text = days[it],
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, false)
                    )
                }
            }
            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            )
            repeat(6) { w ->
                if (cdr[w].any { it?.month == date.month }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        repeat(7) { d ->
                            CalendarDayItem(
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .fillMaxWidth()
                                    .weight(1f, false),
                                date = cdr[w][d],
                                isInRange = (cdr[w][d] ?: currentDate) in startDate..endDate,
                                isOtherMonth = cdr[w][d]?.month != date.month
                            ) {
                                if (editStart) {
                                    startDate = it ?: getCurrentDateTime()
                                    if (startDate >= endDate) startDate = endDate
                                } else {
                                    endDate = it ?: getCurrentDateTime()
                                    if (endDate <= startDate) endDate = startDate
                                }
                                onSelect(it ?: getCurrentDateTime())
                            }
                        }
                    }
                } else if (w > 4) Spacer(Modifier.height(48.dp))
            }
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
                interactionSource = MutableInteractionSource()
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
