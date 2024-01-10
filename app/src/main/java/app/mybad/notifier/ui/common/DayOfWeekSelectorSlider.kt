package app.mybad.notifier.ui.common

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.mybad.utils.dayShortDisplay
import app.mybad.utils.displayDateTime
import app.mybad.utils.firstDayOfMonth
import app.mybad.utils.getDaysOfMonth
import app.mybad.utils.isNotEqualsDay
import app.mybad.utils.minusMonths
import app.mybad.utils.plusDays
import app.mybad.utils.plusMonths
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DayOfWeekSelectorSlider(
    date: LocalDateTime,
    dateReinitialize: Boolean = false,
    onChangeData: (LocalDateTime) -> Unit = {},
    onChangeMonth: (Int) -> Unit = {},
) {
    var datePage by remember(date) {
        mutableStateOf(date.firstDayOfMonth().minusMonths(1))
    }
    val daysMonth by remember(datePage) {
        mutableStateOf(
            Triple(
                datePage.getDaysOfMonth(),
                datePage.plusMonths(1).getDaysOfMonth(),
                datePage.plusMonths(2).getDaysOfMonth()
            )
        )
    }
    val dayInit by remember(daysMonth.first) {
        mutableIntStateOf(daysMonth.first + date.dayOfMonth - 1)
    }
    val pagerState = rememberPagerState(dayInit) {
        daysMonth.first + daysMonth.second + daysMonth.third
    }
    val scope = rememberCoroutineScope()
    Log.w(
        "VTTAG",
        "NotificationMonthPager::DayOfWeekSelectorSlider: init datePage=${datePage.displayDateTime()} date=${date.displayDateTime()}"
    )

    if (dateReinitialize) {
        LaunchedEffect(dayInit) {
            scope.launch {
                Log.e(
                    "VTTAG",
                    "NotificationMonthPager::DayOfWeekSelectorSlider: change dayInit=$dayInit"
                )
                pagerState.scrollToPage(dayInit)
            }
        }
    }

    LaunchedEffect(pagerState.currentPage, datePage) {
        scope.launch {
            Log.d(
                "VTTAG",
                "NotificationMonthPager::DayOfWeekSelectorSlider: ----------------------------------------"
            )
            Log.w(
                "VTTAG",
                "NotificationMonthPager::DayOfWeekSelectorSlider: date=${date.displayDateTime()} currentPage=${pagerState.currentPage} ${daysMonth.first} ${daysMonth.first + daysMonth.second}"
            )
            val newDate = datePage.plusDays(pagerState.currentPage)
            if (pagerState.currentPage < daysMonth.first ||
                pagerState.currentPage >= daysMonth.first + daysMonth.second
            ) {
                val nevIndex = pagerState.currentPage +
                    if (pagerState.currentPage < daysMonth.first) {
                        onChangeMonth(-1)
                        datePage.minusMonths(1).getDaysOfMonth()
                    } else {
                        onChangeMonth(1)
                        -daysMonth.first
                    }
                Log.d(
                    "VTTAG",
                    "NotificationMonthPager::DayOfWeekSelectorSlider: change month datePage=${datePage.displayDateTime()} page=${pagerState.currentPage} nevIndex=$nevIndex"
                )
                pagerState.scrollToPage(nevIndex)
            }
            if (newDate.isNotEqualsDay(date) && !(newDate.dayOfMonth == date.dayOfMonth && newDate.monthNumber != date.monthNumber)) {
                Log.d(
                    "VTTAG",
                    "NotificationMonthPager::DayOfWeekSelectorSlider: init newDate=${newDate.displayDateTime()} date=${date.displayDateTime()}"
                )
                datePage = newDate.firstDayOfMonth().minusMonths(1)
                onChangeData(newDate)
            }
            Log.d(
                "VTTAG",
                "NotificationMonthPager::DayOfWeekSelectorSlider: ----------------------------------------"
            )
        }
    }

    DayOfWeekPager(
        pagerState = pagerState,
        datePage = datePage,
    ) {
        scope.launch {
            Log.w(
                "VTTAG",
                "NotificationMonthPager::DayOfWeekSelectorSlider: animateScrollToPage=$it"
            )
            pagerState.scrollToPage(it)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DayOfWeekPager(
    pagerState: PagerState,
    datePage: LocalDateTime,
    modifier: Modifier = Modifier
        .fillMaxWidth(),
    onClick: (Int) -> Unit = {},
) {
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        pageSpacing = 8.dp,
        contentPadding = PaddingValues(
            horizontal = ((Resources.getSystem().configuration.screenWidthDp - 40) / 2).dp
        ),
        pageSize = PageSize.Fixed(40.dp),
    ) { index ->
        val selected = pagerState.currentPage == index
        val day = datePage.plusDays(index)
        val dayName = day.dayShortDisplay()

        Surface(
            modifier = Modifier
                .height(50.dp)
                .width(40.dp)
                .alpha(if (selected) 1f else 0.5f)
                .clickable { onClick(index) },
            shape = MaterialTheme.shapes.small,
            border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
            color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
            contentColor = if (selected) MaterialTheme.colorScheme.surfaceBright
            else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = AnnotatedString(day.dayOfMonth.toString()),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = AnnotatedString(dayName),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
