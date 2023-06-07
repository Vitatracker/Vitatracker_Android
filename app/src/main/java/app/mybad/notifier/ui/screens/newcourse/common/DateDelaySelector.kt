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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.R
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.notifier.ui.theme.Typography
import java.time.Period
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DateDelaySelector(
    modifier: Modifier = Modifier,
    initValue: Period,
    onSelect: (Period) -> Unit
) {
    val days = (0..30).toList()
    val months = (0..12).toList()
    val pagerStateMonths = rememberPagerState(initialPage = months.size * 10000 + initValue.months)
    val pagerStateDays = rememberPagerState(initialPage = days.size * 10000 + initValue.days)
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
                VerticalPager(
                    pageCount = Int.MAX_VALUE,
                    state = pagerStateMonths,
                    pageSpacing = 8.dp,
                    contentPadding = PaddingValues(top = 80.dp, bottom = 90.dp),
                    pageSize = PageSize.Fixed(32.dp),
                    modifier = Modifier.height(200.dp)
                ) {
                    val ts = when ((pagerStateMonths.currentPage - it).absoluteValue) {
                        0 -> 1f
                        1 -> 0.85f
                        else -> 0.7f
                    }
                    val a = when ((pagerStateMonths.currentPage - it).absoluteValue) {
                        0 -> 1f;1 -> 0.5f;2 -> 0.3f
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
                        val t = if (months[it % months.size] < 10) "${months[it % months.size]}" else "${months[it % months.size]}"
                        Text(text = t, style = Typography.headlineLarge, fontSize = 20.sp)
                    }
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.add_course_days),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                VerticalPager(
                    pageCount = Int.MAX_VALUE,
                    state = pagerStateDays,
                    pageSpacing = 8.dp,
                    contentPadding = PaddingValues(top = 80.dp, bottom = 90.dp),
                    pageSize = PageSize.Fixed(32.dp),
                    modifier = Modifier.height(200.dp)
                ) {
                    val ts = when ((pagerStateDays.currentPage - it).absoluteValue) {
                        0 -> 1f
                        1 -> 0.85f
                        else -> 0.7f
                    }
                    val a = when ((pagerStateDays.currentPage - it).absoluteValue) {
                        0 -> 1f;1 -> 0.5f;2 -> 0.3f
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
                        val t = if (days[it % days.size] < 10) "${days[it % days.size]}" else "${days[it % days.size]}"
                        Text(text = t, style = Typography.headlineLarge, fontSize = 20.sp)
                    }
                }
            }
            Spacer(Modifier.width(0.dp))
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                val newTime = Period.ofDays(0)
                    .withMonths(pagerStateMonths.currentPage % months.size)
                    .withDays(pagerStateDays.currentPage % days.size)
                onSelect(newTime)
            },
            content = { Text(text = stringResource(R.string.settings_save)) }
        )
    }

}

@Preview
@Composable
fun DateSelectorPreview() {
    MyBADTheme {
        DateDelaySelector(initValue = Period.ZERO, onSelect = {})
    }
}