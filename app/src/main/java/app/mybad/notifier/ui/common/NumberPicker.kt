package app.mybad.notifier.ui.common

import android.annotation.SuppressLint
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.material3.MaterialTheme
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
import app.mybad.notifier.ui.theme.Typography
import app.mybad.utils.toText
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NumberPicker(
    pagerState: PagerState,
    items: List<Int>,
    correction: Int,
    pageSize: Dp = 32.dp,
    contentPadding: PaddingValues = PaddingValues(top = 80.dp, bottom = 90.dp),
) {
    val coroutineScope = rememberCoroutineScope()
    val pageLast = items.lastIndex - correction
    var scrollFirstLast = items.size > correction

    VerticalPager(
        modifier = Modifier
            .height(220.dp),
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
