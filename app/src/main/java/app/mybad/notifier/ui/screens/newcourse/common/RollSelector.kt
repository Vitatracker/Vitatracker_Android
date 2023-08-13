package app.mybad.notifier.ui.screens.newcourse.common

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RollSelector(
    modifier: Modifier = Modifier,
    list: List<String>,
    startOffset: Int = 0,
    onSelect: (Int) -> Unit = {},
) {
    val pagerState = rememberPagerState(initialPage = list.size * 10000 + startOffset) { list.size }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        VerticalPager(
            modifier = Modifier.height(200.dp),
            state = pagerState,
            pageSpacing = 8.dp,
            contentPadding = PaddingValues(top = 80.dp, bottom = 90.dp),
            pageSize = PageSize.Fixed(32.dp)
        ) {
            val ts = when ((pagerState.currentPage - it).absoluteValue) {
                0 -> 1f
                1 -> 0.85f
                else -> 0.7f
            }
            val a = when ((pagerState.currentPage - it).absoluteValue) {
                0 -> 1f; 1 -> 0.5f; 2 -> 0.3f
                else -> 0f
            }
            val scale by animateFloatAsState(
                targetValue = ts,
                animationSpec = tween(300, 0, LinearOutSlowInEasing),
                label = ""
            )
            val alpha by animateFloatAsState(
                targetValue = a,
                animationSpec = tween(300, 0, LinearOutSlowInEasing),
                label = ""
            )
            Surface(
                color = MaterialTheme.colorScheme.background,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .alpha(alpha)
                    .wrapContentWidth()
                    .scale(scale)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = list[it % list.size],
                        style = Typography.bodyLarge.copy(fontSize = 16.sp),
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onSelect(pagerState.currentPage % list.size) },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            content = { Text(text = stringResource(R.string.settings_save)) }
        )
    }
}
