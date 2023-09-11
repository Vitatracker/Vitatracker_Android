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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.BottomSlideInDialog
import app.mybad.notifier.ui.common.MonthSelector
import app.mybad.notifier.ui.common.TitleText
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import app.mybad.utils.DAYS_A_WEEK
import app.mybad.utils.atStartOfMonth
import app.mybad.utils.currentDateTime
import app.mybad.utils.dayShortDisplay
import app.mybad.utils.minusDays
import app.mybad.utils.plusDays
import app.mybad.utils.toSecondsLeftFromStartOfDay
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    state: CalendarContract.State,
    effectFlow: Flow<CalendarContract.Effect>,
    sendEvent: (event: CalendarContract.Event) -> Unit = {},
    navigation: (navigationEffect: CalendarContract.Effect.Navigation) -> Unit,
) {
//    var date by remember { mutableStateOf(currentDateTime()) }
//    var selectedDate by remember { mutableStateOf<LocalDateTime?>(date) }
    var dialogIsShown by remember { mutableStateOf(false) }
//    var usagesDaily = collectUsagesToday(
//        date = selectedDate,
//        usages = state.patternUsage
//    )
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow.collect { effect ->
            when (effect) {
                is CalendarContract.Effect.Navigation -> navigation(effect)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(title = { TitleText(textStringRes = R.string.calendar_title) })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MonthSelector(
                date = state.date,
                onSwitch = { sendEvent(CalendarContract.Event.ChangeDate(date = it)) }
            )
            Spacer(Modifier.height(16.dp))
            CalendarItem(
                date = state.date,
                usages = state.patternUsage,
                onSelect = {
                    sendEvent(CalendarContract.Event.SelectDate(date = it))
                    dialogIsShown = true
                }
            )
            if (dialogIsShown) {
                BottomSlideInDialog(onDismissRequest = { dialogIsShown = false }) {
                    DailyUsages(
                        date = state.selectedDate,
                        usages = state.usagesDaily,
                        onDismiss = { dialogIsShown = false },
                        onNewDate = {
                            sendEvent(CalendarContract.Event.SelectDate(date = it))
                            //TODO("изменить реализацию, загружать usagesDaily при изменении selectedDate")
                            sendEvent(CalendarContract.Event.ChangeUsagesDaily)
                        },
                        onUsed = { sendEvent(CalendarContract.Event.SetUsage(it)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarItem(
    date: LocalDateTime,
    usages: List<UsageDisplayDomainModel>,
    onSelect: (LocalDateTime?) -> Unit = {}
) {
    val currentDate = currentDateTime()
    val cdr: Array<Array<LocalDateTime?>> = Array(6) {
        Array(DAYS_A_WEEK) { null }
    }
    val usgs = Array(6) {
        Array(DAYS_A_WEEK) { listOf<UsageDisplayDomainModel>() }
    }
    TODO("епределать реализацию")
    // не понятно что это, начало месяца или что или последний день предыдущего месяца
    // minusDays(date.dayOfMonth.toLong())
    val fwd = date.atStartOfMonth()
    repeat(6) { w ->
        repeat(DAYS_A_WEEK) { day ->
            if (w == 0 && day < fwd.dayOfWeek.value) {
                val time = fwd.minusDays(fwd.dayOfWeek.ordinal - day)
                usgs[w][day] = usages.filter {
                    it.useTime.toSecondsLeftFromStartOfDay() == time.toSecondsLeftFromStartOfDay()
                }
                cdr[w][day] = time
            } else {
                val time = fwd.plusDays(w * DAYS_A_WEEK + day - fwd.dayOfWeek.ordinal)
                usgs[w][day] = usages.filter {
                    it.useTime.toSecondsLeftFromStartOfDay() == time.toSecondsLeftFromStartOfDay()
                }
                cdr[w][day] = time
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
            repeat(DAYS_A_WEEK) { day ->
                Text(
                    text = day.dayShortDisplay().uppercase(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(40.dp)
                )
            }
        }
        HorizontalDivider(
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
    date: LocalDateTime? = null,
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

/*
private fun collectUsagesToday(
    date: LocalDateTime?,
    usages: List<UsageDomainModel>,
): List<UsageDomainModel> {
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
*/
