package app.mybad.notifier.ui.screens.newcourse.common

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
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import kotlinx.datetime.DateTimePeriod
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DaysSelector(
    modifier: Modifier = Modifier,
    initValue: Int,
    onSelect: (Int) -> Unit
) {
    val days = (0..90).toList()
    val pagerStateDays = rememberPagerState(initialPage = initValue) { days.size }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
            .width(200.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.add_course_days),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            VerticalPager(
                modifier = Modifier.height(200.dp),
                state = pagerStateDays,
                pageSpacing = 8.dp,
                contentPadding = PaddingValues(top = 80.dp, bottom = 90.dp),
                pageSize = PageSize.Fixed(32.dp)
            ) {
                val ts = when ((pagerStateDays.currentPage - it).absoluteValue) {
                    0 -> 1f
                    1 -> 0.85f
                    else -> 0.7f
                }
                val a = when ((pagerStateDays.currentPage - it).absoluteValue) {
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
                    val t = if (days[it % days.size] < 10) "${days[it % days.size]}"
                    else "${days[it % days.size]}"
                    Text(text = t, style = Typography.headlineLarge, fontSize = 20.sp)
                }
            }
        }
        ReUseFilledButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textId = R.string.settings_save,
            onClick = {
                onSelect(pagerStateDays.currentPage % days.size)
            }
        )
    }
}

@Preview
@Composable
fun DaysPreview() {
    MyBADTheme {
        DaysSelector(
            initValue = 2,
            onSelect = {}
        )
    }
}
