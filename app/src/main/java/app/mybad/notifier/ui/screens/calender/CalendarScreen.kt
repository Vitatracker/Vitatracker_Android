package app.mybad.notifier.ui.screens.calender

import android.util.Log
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.ui.screens.common.BottomSlideInDialog
import app.mybad.notifier.ui.screens.common.MonthSelector
import app.mybad.notifier.ui.screens.reuse.TitleText
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.utils.DAYS_A_WEEK
import app.mybad.notifier.utils.atEndOfDay
import app.mybad.notifier.utils.atStartOfDay
import app.mybad.notifier.utils.atStartOfMonth
import app.mybad.notifier.utils.dayShortDisplay
import app.mybad.notifier.utils.getCurrentDateTime
import app.mybad.notifier.utils.minusDays
import app.mybad.notifier.utils.plusDays
import app.mybad.notifier.utils.toDateFullDisplay
import app.mybad.notifier.utils.toEpochSecond
import app.mybad.notifier.utils.toSecondsLeftFromStartOfDay
import app.mybad.theme.R
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen() {
    val viewModel: CalendarViewModel = hiltViewModel()
    val usages: List<UsageCommonDomainModel> = listOf()
    val meds: List<MedDomainModel> = listOf()
    var date by remember { mutableStateOf(getCurrentDateTime()) }
    var selectedDate: LocalDateTime? by remember { mutableStateOf(date) }
    var dialogIsShown by remember { mutableStateOf(false) }
    var daily = collectUsagesToday(
        date = selectedDate,
        usages = usages
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleText(textStringRes = R.string.calendar_h) }
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MonthSelector(
                date = date,
                onSwitch = { date = it }
            )
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
                        onUsed = { viewModel.onUsed(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarScreenItem(
    date: LocalDateTime,
    usages: List<UsageCommonDomainModel>,
    onSelect: (LocalDateTime?) -> Unit
) {
    val currentDate = getCurrentDateTime()
    val cdr: Array<Array<LocalDateTime?>> = Array(6) {
        Array(DAYS_A_WEEK) { null }
    }
    val usgs = Array(6) {
        Array(DAYS_A_WEEK) { listOf<UsageCommonDomainModel>() }
    }
    // не понятно что это, начало месяца или что или последний день предыдущего месяца
    // minusDays(date.dayOfMonth.toLong())
    val fwd = date.atStartOfMonth()
    repeat(6) { w ->
        repeat(DAYS_A_WEEK) { d ->
            if (w == 0 && d < fwd.dayOfWeek.value) {
                val time = fwd.minusDays(fwd.dayOfWeek.ordinal - d)
                usgs[w][d] = usages.filter {
                    it.useTime.toSecondsLeftFromStartOfDay() == time.toSecondsLeftFromStartOfDay()
                }
                cdr[w][d] = time
            } else {
                val time = fwd.plusDays(w * DAYS_A_WEEK + d - fwd.dayOfWeek.ordinal)
                usgs[w][d] = usages.filter {
                    it.useTime.toSecondsLeftFromStartOfDay() == time.toSecondsLeftFromStartOfDay()
                }
                cdr[w][d] = time
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
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
            (1..DAYS_A_WEEK).forEach { day ->
                Text(
                    text = day.dayShortDisplay().uppercase(),
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
                    repeat(DAYS_A_WEEK) { day ->
                        CalendarDayItem(
                            date = cdr[w][day],
                            usages = usgs[w][day].size,
                            isSelected = cdr[w][day]?.dayOfYear == currentDate.dayOfYear &&
                                    cdr[w][day]?.year == currentDate.year,
                            isOtherMonth = date.month.value != cdr[w][day]?.month?.value
                        ) { onSelect(it) }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayItem(
    usages: Int = 0,
    date: LocalDateTime?,
    isSelected: Boolean = false,
    isOtherMonth: Boolean = false,
    onSelect: (LocalDateTime?) -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(bottom = 8.dp)
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
    Log.w(
        "VTTAG",
        "CalendarScreen::collectUsagesToday: date=${date?.toDateFullDisplay()} usages=${usages.size}"
    )

    if (date == null) return usages
    val fromTime = date.atStartOfDay().toEpochSecond(isUTC = false)
    val toTime = date.atEndOfDay().toEpochSecond(isUTC = false)
    return usages.filter { it.useTime in fromTime..toTime }.also {
        Log.w(
            "VTTAG",
            "CalendarScreen::collectUsagesToday: date=${date.toDateFullDisplay()} usages=${it.size}"
        )

    }
}
