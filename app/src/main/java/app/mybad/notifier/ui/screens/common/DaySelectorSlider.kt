package app.mybad.notifier.ui.screens.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import app.mybad.notifier.R
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.YearMonth

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DaySelectorSlider(
    modifier: Modifier = Modifier,
    date: LocalDateTime? = null,
    onSelect: (LocalDateTime?) -> Unit = {}
) {
    val selectedDate = date ?: LocalDateTime.now()!!
    val yearMonth = YearMonth.of(selectedDate.year, selectedDate.month)
    val monthLength = yearMonth.lengthOfMonth()
    val pagerState = rememberPagerState(initialPage = selectedDate.dayOfMonth) { monthLength }
    val scope = rememberCoroutineScope()
    val padding = PaddingValues(horizontal = (LocalConfiguration.current.screenWidthDp / 2 + 20 + 8).dp)
    val dow = stringArrayResource(R.array.days_short)

    HorizontalPager(
        modifier = modifier.fillMaxWidth(),
        state = pagerState,
        pageSpacing = 8.dp,
        userScrollEnabled = true,
        reverseLayout = false,
        contentPadding = padding,
        beyondBoundsPageCount = 0,
        pageSize = PageSize.Fixed(40.dp),
        key = null,
        pageContent = {
            val itsDate = date?.withDayOfMonth(it + 1)
            val isSelected = itsDate?.isEqual(date) ?: false
            Surface(
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = if (isSelected) 1f else 0.5f)),
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                modifier = Modifier
                    .height(50.dp)
                    .width(40.dp)
                    .alpha(if (isSelected) 1f else 0.5f)
                    .clickable {
                        onSelect(itsDate)
                        scope.launch { pagerState.animateScrollToPage(itsDate?.dayOfMonth ?: 0) }
                    }
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(
                            text = "${itsDate?.dayOfMonth}",
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.Unspecified
                        )
                        Text(
                            text = dow[(itsDate?.dayOfWeek?.value ?: 1) - 1],
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.Unspecified
                        )
                    }
                }
            }
        }
    )
}
