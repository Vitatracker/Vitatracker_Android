package app.mybad.notifier.ui.screens.newcourse.common

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.utils.toText
import app.mybad.theme.R
import app.mybad.utils.displayTimeInMinutes
import app.mybad.utils.hour
import app.mybad.utils.hourPlusMinute
import app.mybad.utils.minute
import app.mybad.utils.timeInMinutesSystemToUTC
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

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
                contentPadding = PaddingValues(top = 80.dp, bottom = 90.dp),
                pageSize = 32.dp
            )
            NumberPicker(
                pagerState = pagerStateMinutes,
                items = minutes,
                correction = correction,
                contentPadding = PaddingValues(top = 80.dp, bottom = 90.dp),
                pageSize = 32.dp
            )
            Spacer(Modifier.width(0.dp))
        }
        ReUseFilledButton(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            textId = R.string.settings_save,
            onClick = {
                val newTime = hours[pagerStateHours.currentPage % hours.size]
                    .hourPlusMinute(minutes[pagerStateMinutes.currentPage % minutes.size])
                Log.w(
                    "VTTAG",
                    "TimeSelector::onSelect: date time=${newTime.displayTimeInMinutes()} - UTC=${newTime.timeInMinutesSystemToUTC().displayTimeInMinutes()}"
                )
                onSelect(newTime)
            }
        )
    }

}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NumberPicker(
    pagerState: PagerState,
    items: List<Int>,
    contentPadding: PaddingValues,
    pageSize: Dp,
    correction: Int,
) {
    val coroutineScope = rememberCoroutineScope()
    val pageLast = items.lastIndex - correction
    var scrollFirstLast = items.size > correction

    VerticalPager(
        modifier = Modifier
            .height(200.dp),
        state = pagerState,
        pageSpacing = 21.dp, // между элементами
        contentPadding = contentPadding,
        pageSize = PageSize.Fixed(pageSize), // размер элемента
    ) { index ->
        // прокрутка вперед на первый или прокрутка назад на последний
        if (scrollFirstLast && (pagerState.settledPage < correction || pagerState.settledPage > pageLast)) {
            scrollFirstLast = false
            coroutineScope.launch {
                pagerState.scrollToPage(
                    if (pagerState.settledPage < correction) {
                        pagerState.settledPage + pageLast - correction + 1
                    } else {
                        pagerState.settledPage - pageLast + correction - 1
                    }
                )
                scrollFirstLast = true
            }
        }
        val ts = when ((pagerState.currentPage - index).absoluteValue) {
            0 -> 1f
            1 -> 0.85f
            else -> 0.7f
        }
        val a = when ((pagerState.currentPage - index).absoluteValue) {
            0 -> 1f
            1 -> 0.5f
            2 -> 0.3f
            else -> 0f
        }
        val scale by animateFloatAsState(
            targetValue = ts,
            animationSpec = tween(300, 0, LinearOutSlowInEasing),
            label = "",
        )
        val alpha by animateFloatAsState(
            targetValue = a,
            animationSpec = tween(300, 0, LinearOutSlowInEasing),
            label = "",
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .alpha(alpha)
                .wrapContentWidth()
                .scale(scale)
        ) {
            Text(
                text = items[index % items.size].toText(),
                style = Typography.headlineLarge,
                fontSize = 20.sp
            )
        }
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
