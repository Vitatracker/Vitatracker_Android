package app.mybad.notifier.ui.screens.newcourse.common

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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.R
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.utils.changeTime
import app.mybad.notifier.utils.toEpochSecond
import app.mybad.notifier.utils.toLocalDateTime
import app.mybad.notifier.utils.toSystemDateTime
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimeSelector(
    modifier: Modifier = Modifier,
    initTime: Long,
    onSelect: (Long) -> Unit
) {
    val hours = (0..23).toList()
    val minutes = (0..59).toList()
    val time = initTime.toSystemDateTime()
    val pagerStateHours = rememberPagerState(initialPage = time.hour) { hours.size }
    val pagerStateMinutes = rememberPagerState(initialPage = time.minute) { minutes.size }

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
                contentPadding = PaddingValues(top = 80.dp, bottom = 90.dp),
                pageSize = 32.dp
            )
            NumberPicker(
                pagerState = pagerStateMinutes,
                items = minutes,
                contentPadding = PaddingValues(top = 80.dp, bottom = 90.dp),
                pageSize = 32.dp
            )
            Spacer(Modifier.width(0.dp))
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                val newTime = time.changeTime(
                    hour = pagerStateHours.currentPage % hours.size,
                    minute = pagerStateMinutes.currentPage % minutes.size,
                ).toEpochSecond(isUTC = false)
                Log.w(
                    "VTTAG",
                    "TimeSelector::onSelect: date time=${newTime.toSystemDateTime()} = ${newTime.toLocalDateTime()}"
                )
                onSelect(newTime)
            },
            content = { Text(text = stringResource(R.string.settings_save)) }
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NumberPicker(
    pagerState: PagerState,
    items: List<Int>,
    contentPadding: PaddingValues,
    pageSize: Dp
) {
    VerticalPager(
        modifier = Modifier
            .height(200.dp),
        state = pagerState,
        pageSpacing = 8.dp,
        contentPadding = contentPadding,
        pageSize = PageSize.Fixed(pageSize)
    ) {
        val ts = when ((pagerState.currentPage - it).absoluteValue) {
            0 -> 1f
            1 -> 0.85f
            else -> 0.7f
        }
        val a = when ((pagerState.currentPage - it).absoluteValue) {
            0 -> 1f
            1 -> 0.5f
            2 -> 0.3f
            else -> 0f
        }
        val scale by animateFloatAsState(
            targetValue = ts,
            animationSpec = tween(300, 0, LinearOutSlowInEasing)
        )
        val alpha by animateFloatAsState(
            targetValue = a,
            animationSpec = tween(300, 0, LinearOutSlowInEasing)
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
                text = items[it % items.size].toDisplay(),
                style = Typography.headlineLarge,
                fontSize = 20.sp
            )
        }
    }
}

private fun Int.toDisplay() = "%02d".format(this)
