package app.mybad.notifier.ui.screens.calender

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.BottomSlideInDialog
import app.mybad.notifier.ui.screens.common.MonthSelector
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.utils.getCurrentDateTime
import app.mybad.notifier.utils.minusDays
import app.mybad.notifier.utils.plusDays
import app.mybad.notifier.utils.atEndOfDayInEpochSeconds
import app.mybad.notifier.utils.atStartOfDayInEpochSeconds
import app.mybad.notifier.utils.atStartOfMonth
import app.mybad.notifier.utils.toSecondsLeftFromStartOfDay
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    usages: List<UsageCommonDomainModel>,
    meds: List<MedDomainModel>,
    reducer: (CalendarIntent) -> Unit
) {
    var date by remember { mutableStateOf(getCurrentDateTime()) }
    var selectedDate: LocalDateTime? by remember { mutableStateOf(date) }
    var dialogIsShown by remember { mutableStateOf(false) }
    var daily = collectUsagesToday(
        date = selectedDate,
        usages = usages
    )

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.calendar_h),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 24.dp),
                        color = MaterialTheme.colorScheme.primary,
                        style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            )
            MonthSelector(date = date) { date = it }
            Spacer(Modifier.height(16.dp))
            CalendarScreenItem(
                date = date,
                usages = usages,
                onSelect = {
                    selectedDate = it
                    dialogIsShown = true
                }
            )
            if (dialogIsShown) {
                BottomSlideInDialog(onDismissRequest = { dialogIsShown = false }) {
                    DailyUsages(
                        date = selectedDate,
                        dayData = daily,
                        meds = meds,
                        onDismiss = { dialogIsShown = false },
                        onNewDate = {
                            selectedDate = it
                            daily = collectUsagesToday(
                                date = selectedDate,
                                usages = usages
                            )
                        },
                        onUsed = { reducer(CalendarIntent.SetUsage(it)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarScreenItem(
    modifier: Modifier = Modifier,
    date: LocalDateTime,
    usages: List<UsageCommonDomainModel>,
    onSelect: (LocalDateTime?) -> Unit
) {
    val days = stringArrayResource(R.array.days_short)
    val currentDate = getCurrentDateTime()
    val cdr: Array<Array<LocalDateTime?>> = Array(6) {
        Array(7) { null }
    }
    val usgs = Array(6) {
        Array(7) { listOf<UsageCommonDomainModel>() }
    }
    // не понятно что это, начало месяца или что или последний день предыдущего месяца
    // minusDays(date.dayOfMonth.toLong())
    val fwd = date.atStartOfMonth()
    for (w in 0..5) {
        for (d in 0..6) {
            if (w == 0 && d < fwd.dayOfWeek.value) {
                val time = fwd.minusDays(fwd.dayOfWeek.ordinal - d)
                usgs[w][d] =
                    usages.filter { it.useTime.toSecondsLeftFromStartOfDay() == time.toSecondsLeftFromStartOfDay() }
                cdr[w][d] = time
            } else {
                val time = fwd.plusDays(w * 7 + d - fwd.dayOfWeek.ordinal)
                usgs[w][d] =
                    usages.filter { it.useTime.toSecondsLeftFromStartOfDay() == time.toSecondsLeftFromStartOfDay() }
                cdr[w][d] = time
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
                        text = days[it].uppercase(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(40.dp)
                    )
                }
            }
            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
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
                                modifier = Modifier.padding(bottom = 8.dp),
                                date = cdr[w][d],
                                usages = usgs[w][d].size,
                                isSelected = cdr[w][d]?.dayOfYear == currentDate.dayOfYear && cdr[w][d]?.year == currentDate.year,
                                isOtherMonth = date.month.value != cdr[w][d]?.month?.value
                            ) { onSelect(it) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayItem(
    modifier: Modifier = Modifier,
    usages: Int = 0,
    date: LocalDateTime?,
    isSelected: Boolean = false,
    isOtherMonth: Boolean = false,
    onSelect: (LocalDateTime?) -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .border(
                1.dp,
                if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                CircleShape
            )
            .alpha(if (isOtherMonth) 0.5f else 1f)
            .clickable { if (usages > 0) onSelect(date) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val indicatorColor = if (usages > 0 && isSelected) {
                MaterialTheme.colorScheme.primary
            } else if (usages > 0) {
                Color.LightGray
            } else {
                Color.Transparent
            }
            Box(
                Modifier
                    .size(5.dp)
                    .background(
                        color = indicatorColor,
                        shape = CircleShape
                    )
            )
            Text(
                text = date?.dayOfMonth.toString(),
                style = Typography.bodyLarge,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified
            )
        }
    }
}

private fun collectUsagesToday(
    date: LocalDateTime?,
    usages: List<UsageCommonDomainModel>,
): List<UsageCommonDomainModel> {
    if (date == null) return usages
    val fromTime = date.atStartOfDayInEpochSeconds()
    val toTime = date.atEndOfDayInEpochSeconds()
    return usages.filter { it.useTime in fromTime..toTime }
}
