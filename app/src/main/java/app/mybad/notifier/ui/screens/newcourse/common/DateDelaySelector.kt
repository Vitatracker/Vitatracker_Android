package app.mybad.notifier.ui.screens.newcourse.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.common.NumberPicker
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.theme.R
import kotlinx.datetime.DateTimePeriod

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DateDelaySelector(
    modifier: Modifier = Modifier,
    initValue: DateTimePeriod,
    onSelect: (DateTimePeriod) -> Unit
) {
    val months = listOf(10, 11, 12) + (0..12).toList() + listOf(0, 1, 2)
    val days = listOf(28, 29, 30) + (0..30).toList() + listOf(0, 1, 2)
    val correction = 3

    val pagerStateMonths = rememberPagerState(initialPage = initValue.months + correction)
    { months.size }
    val pagerStateDays = rememberPagerState(initialPage = initValue.days + correction)
    { days.size }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
            .width(200.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.width(0.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.add_course_months),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                NumberPicker(
                    pagerState = pagerStateMonths,
                    items = months,
                    correction = correction,
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.add_course_days),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                NumberPicker(
                    pagerState = pagerStateDays,
                    items = days,
                    correction = correction,
                )
            }
            Spacer(Modifier.width(0.dp))
        }
        ReUseFilledButton(
            textId = R.string.settings_save,
            onClick = {
                val newTime = DateTimePeriod(
                    months = months[pagerStateMonths.currentPage % months.size],
                    days = days[pagerStateDays.currentPage % days.size],
                )
                onSelect(newTime)
            }
        )
    }
}

@Preview
@Composable
fun DateSelectorPreview() {
    MyBADTheme {
        DateDelaySelector(initValue = DateTimePeriod(days = 5, months = 4),
            onSelect = {}
        )
    }
}
