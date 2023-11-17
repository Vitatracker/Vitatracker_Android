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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.BottomSlideInDialog
import app.mybad.notifier.ui.common.MonthSelector
import app.mybad.notifier.ui.common.TitleText
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import app.mybad.utils.DAYS_A_WEEK
import app.mybad.utils.WEEKS_PER_MONTH
import app.mybad.utils.currentDateTimeSystem
import app.mybad.utils.dayShortDisplay
import app.mybad.utils.displayDateTime
import app.mybad.utils.isEqualsMonth
import app.mybad.utils.isNotEqualsMonth
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    state: CalendarContract.State,
    dateUpdate: Flow<LocalDateTime>,
    effectFlow: Flow<CalendarContract.Effect>,
    sendEvent: (event: CalendarContract.Event) -> Unit = {},
    navigation: (navigationEffect: CalendarContract.Effect.Navigation) -> Unit,
) {

    val currentDate = dateUpdate.collectAsStateWithLifecycle(initialValue = currentDateTimeSystem())

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
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Log.w("VTTAG", "CalendarViewModel::MonthSelector: date=${state.date.displayDateTime()}")
            MonthSelector(
                date = state.date,
                onSwitch = { sendEvent(CalendarContract.Event.ChangeMonth(date = it)) }
            )
            Spacer(Modifier.height(16.dp))
            CalendarItem(
                date = state.date,
                currentDate = currentDate.value,
                datesWeeks = state.datesWeeks,
                usagesWeeks = state.usagesWeeks,
                onSelect = { sendEvent(CalendarContract.Event.SelectElement(it)) }
            )
            state.selectedElement?.let { element ->
                Log.w(
                    "VTTAG",
                    "CalendarViewModel::selectElement: selectElement=${state.selectedElement}"
                )
                // проверка
                val date = state.datesWeeks[element.first][element.second]
                val usages = state.usagesWeeks[element.first][element.second]
                if (usages.isEmpty()) {
                    sendEvent(CalendarContract.Event.SelectElement(null))
                } else {
                    BottomSlideInDialog(
                        onDismissRequest = { sendEvent(CalendarContract.Event.SelectDate(null)) }
                    ) {
                        DailyUsages(
                            date = date,
                            usagesDisplay = usages,
                            onDismiss = { sendEvent(CalendarContract.Event.SelectDate(null)) },
                            onNewDate = { sendEvent(CalendarContract.Event.SelectDate(it)) },
                            onUsed = { sendEvent(CalendarContract.Event.SetUsage(it)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarItem(
    date: LocalDateTime,
    currentDate: LocalDateTime,
    datesWeeks: Array<Array<LocalDateTime>>,
    usagesWeeks: Array<Array<List<UsageDisplayDomainModel>>>,
    onSelect: (Pair<Int, Int>) -> Unit = {}
) {
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
                            date = datesWeeks[week][day],
                            usages = usagesWeeks[week][day].size,
                            isSelected = datesWeeks[week][day].dayOfYear == currentDate.dayOfYear &&
                                datesWeeks[week][day].year == currentDate.year,
                            isOtherMonth = datesWeeks[week][day].isNotEqualsMonth(date)
                        ) {
                            onSelect(week to day) // передаем выбранный элемент массива
                        }
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
    onClick: () -> Unit = {}
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
            .clickable { if (usages > 0) onClick() }
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
