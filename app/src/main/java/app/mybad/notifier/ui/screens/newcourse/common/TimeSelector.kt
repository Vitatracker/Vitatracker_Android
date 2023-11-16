package app.mybad.notifier.ui.screens.newcourse.common

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.mybad.notifier.ui.common.NumberPicker
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.theme.R
import app.mybad.utils.displayTimeInMinutes
import app.mybad.utils.hour
import app.mybad.utils.hourPlusMinute
import app.mybad.utils.minute

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
            .width(200.dp)
    ) {
        Box {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            ) {}
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(
                        start = 0.dp,
                        top = 40.dp,
                        end = 0.dp,
                        bottom = 0.dp
                    )
                    .fillMaxWidth()
            ) {
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
            }
        }
        ReUseFilledButton(
            textId = R.string.settings_save,
            onClick = {
                val newTime = hours[pagerStateHours.currentPage % hours.size]
                    .hourPlusMinute(minutes[pagerStateMinutes.currentPage % minutes.size])
                Log.w(
                    "VTTAG",
                    "TimeSelector::onSelect: date time=${newTime.displayTimeInMinutes()}"
                )
                onSelect(newTime)
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
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
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            TimeSelector(
                initTime = initTime,
                onSelect = onSelect::invoke
            )
        }
    }
}
