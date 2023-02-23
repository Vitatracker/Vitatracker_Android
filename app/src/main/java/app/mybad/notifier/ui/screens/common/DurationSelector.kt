package app.mybad.notifier.ui.screens.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.mybad.notifier.R
import app.mybad.notifier.ui.theme.Typography
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DurationSelector(
    startDate: Long,
    label: String,
    onSelect: (Long) -> Unit
) {

    val monthsPager = rememberPagerState()
    val daysPager = rememberPagerState()
    val date = LocalDateTime.ofInstant(Instant.ofEpochSecond(startDate), ZoneId.systemDefault())
    var newDate = 0L

    LaunchedEffect(monthsPager.currentPage) {
        newDate = date.plusDays(daysPager.currentPage.toLong())
            .plusMonths(monthsPager.currentPage.toLong()).toEpochSecond(ZoneOffset.UTC)
        onSelect(newDate)
    }
    LaunchedEffect(daysPager.currentPage) {
        newDate = date.plusDays(daysPager.currentPage.toLong())
            .plusMonths(monthsPager.currentPage.toLong()).toEpochSecond(ZoneOffset.UTC)
        onSelect(newDate)
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = Typography.bodyLarge
        )
        Spacer(Modifier.height(4.dp))
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.Unspecified,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_course_months), textAlign = TextAlign.Start,
                    modifier = Modifier.weight(0.3f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    VerticalPager(
                        pageCount = 12,
                        state = monthsPager,
                        horizontalAlignment = Alignment.End
                    ) {
                        var text = (it).toString()
                        if(text.length == 1) text = "0$text"
                        Text(
                            text = text,
                            style = Typography.headlineLarge
                        )
                    }
                    Divider(
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .padding(horizontal = 8.dp)
                            .width(1.dp)
                    )
                    VerticalPager(
                        pageCount = 30,
                        state = daysPager,
                        horizontalAlignment = Alignment.Start
                    ) {
                        var text = it.toString()
                        if(text.length == 1) text = "0$text"
                        Text(
                            text = text,
                            style = Typography.headlineLarge
                        )
                    }
                }
                Text(text = stringResource(R.string.add_course_days), textAlign = TextAlign.End,
                    modifier = Modifier.weight(0.3f))
            }
        }
    }
}