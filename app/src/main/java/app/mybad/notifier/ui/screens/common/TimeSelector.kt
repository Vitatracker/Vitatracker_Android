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
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.theme.Typography

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimeSelector(
    initTime: Int,
    onSelect: (Int) -> Unit
) {

    val hours by remember { mutableStateOf(initTime / 3600) }
    val minutes by remember { mutableStateOf(initTime % 3600 / 60) }
    val hoursPagerState = rememberPagerState(initialPage = hours)
    val minutesPagerState = rememberPagerState(initialPage = minutes)

    LaunchedEffect(hoursPagerState.currentPage) {
        onSelect(hoursPagerState.currentPage * 3600 + minutesPagerState.currentPage * 60)
    }
    LaunchedEffect(minutesPagerState.currentPage) {
        onSelect(hoursPagerState.currentPage * 3600 + minutesPagerState.currentPage * 60)
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.Unspecified,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                VerticalPager(
                    pageCount = 24,
                    state = hoursPagerState,
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
                    pageCount = 60,
                    state = minutesPagerState,
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
        }
    }
}