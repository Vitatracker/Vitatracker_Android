package app.mybad.notifier.ui.screens.newcourse.common

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.mybad.notifier.ui.common.NumberPicker
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.theme.R
import app.mybad.utils.displayTimeInMinutes
import app.mybad.utils.hour
import app.mybad.utils.hourPlusMinute
import app.mybad.utils.minute
import app.mybad.utils.timeInMinutesSystemToUTC

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimeSelector(
    modifier: Modifier = Modifier,
    initTime: Int,// время в минутах с учетом часового пояса
    onSelect: (Int) -> Unit
) {
    val hours = listOf(21, 22, 23) + (0..23).toList() + listOf(0, 1, 2)
    val minutes = listOf(57, 58, 59) + (0..59).toList() + listOf(0, 1, 2)
    val correction = 3

    val pagerStateHours = rememberPagerState(initialPage = initTime.hour() + correction)
    { hours.size }
    val pagerStateMinutes = rememberPagerState(initialPage = initTime.minute() + correction)
    { minutes.size }

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
            NumberPicker(
                pagerState = pagerStateHours,
                items = hours,
                correction = correction,
            )
            NumberPicker(
                pagerState = pagerStateMinutes,
                items = minutes,
                correction = correction,
            )
            Spacer(Modifier.width(0.dp))
        }
        ReUseFilledButton(
            textId = R.string.settings_save,
            onClick = {
                val newTime = hours[pagerStateHours.currentPage % hours.size]
                    .hourPlusMinute(minutes[pagerStateMinutes.currentPage % minutes.size])
                Log.w(
                    "VTTAG",
                    "TimeSelector::onSelect: date time=${newTime.displayTimeInMinutes()} - UTC=${
                        newTime.timeInMinutesSystemToUTC().displayTimeInMinutes()
                    }"
                )
                onSelect(newTime)
            }
        )
    }

}

@Composable
fun TimeSelectorDialog(
    initTime: Int,
    onDismiss: () -> Unit,
    onSelect: (Int) -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            TimeSelector(
                initTime = initTime,
                onSelect = onSelect::invoke
            )
        }
    }
}
