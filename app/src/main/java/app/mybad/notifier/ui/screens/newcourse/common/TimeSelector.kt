package app.mybad.notifier.ui.screens.newcourse.common

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PageSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.R
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.utils.changeTime
import app.mybad.notifier.utils.toEpochSecond
import app.mybad.notifier.utils.toLocalDateTime
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimeSelector(
    modifier: Modifier = Modifier,
    initTime: Long,
    onSelect: (Long) -> Unit
) {
    val minutes = (0..59).toList()
    val hours = (0..23).toList()
    val time = initTime.toLocalDateTime()
    val pagerStateHours = rememberPagerState(initialPage = minutes.size * 10000 + time.hour)
    val pagerStateMinutes = rememberPagerState(initialPage = hours.size * 10000 + time.minute)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp).width(200.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.width(0.dp))
            VerticalPager(
                pageCount = Int.MAX_VALUE,
                state = pagerStateHours,
                pageSpacing = 8.dp,
                contentPadding = PaddingValues(top = 80.dp, bottom = 90.dp),
                pageSize = PageSize.Fixed(32.dp),
                modifier = Modifier.height(200.dp)
            ) {
                val ts = when ((pagerStateHours.currentPage - it).absoluteValue) {
                    0 -> 1f
                    1 -> 0.85f
                    else -> 0.7f
                }
                val a = when ((pagerStateHours.currentPage - it).absoluteValue) {
                    0 -> 1f; 1 -> 0.5f; 2 -> 0.3f
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
                    val t = if(hours[it % hours.size] < 10) "0${hours[it % hours.size]}" else "${hours[it % hours.size]}"
                    Text( text = t, style = Typography.headlineLarge, fontSize = 20.sp)
                }
            }
            VerticalPager(
                pageCount = Int.MAX_VALUE,
                state = pagerStateMinutes,
                pageSpacing = 8.dp,
                contentPadding = PaddingValues(top = 80.dp, bottom = 90.dp),
                pageSize = PageSize.Fixed(32.dp),
                modifier = Modifier.height(200.dp)
            ) {
                val ts = when ((pagerStateMinutes.currentPage - it).absoluteValue) {
                    0 -> 1f
                    1 -> 0.85f
                    else -> 0.7f
                }
                val a = when ((pagerStateMinutes.currentPage - it).absoluteValue) {
                    0 -> 1f; 1 -> 0.5f; 2 -> 0.3f
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
                    val t = if(minutes[it % minutes.size] < 10) "0${minutes[it % minutes.size]}" else "${minutes[it % minutes.size]}"
                    Text(text = t, style = Typography.headlineLarge, fontSize = 20.sp)
                }
            }
            Spacer(Modifier.width(0.dp))
        }
        Button(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                val newTime = time.changeTime(
                    hour = pagerStateHours.currentPage % hours.size,
                    minute = pagerStateMinutes.currentPage % minutes.size,
                ).toEpochSecond()
                onSelect(newTime)
            },
            content = { Text(text = stringResource(R.string.settings_save)) }
        )
    }

}
