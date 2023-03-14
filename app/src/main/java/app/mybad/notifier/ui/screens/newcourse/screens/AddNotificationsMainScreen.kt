package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.ui.screens.newcourse.NewCourseIntent
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.NavigationRow
import app.mybad.notifier.ui.theme.Typography
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

@Composable
@Preview
fun AddNotificationsMainScreen(
    modifier: Modifier = Modifier,
    reducer: (NewCourseIntent) -> Unit = {},
    med: MedDomainModel = MedDomainModel(),
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
) {

    val forms = stringArrayResource(R.array.types)
    var notificationsPattern by remember { mutableStateOf(emptyList<Pair<LocalTime, Int>>()) } //time, quantity
    var dialogIsShown by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                onClick = { dialogIsShown = true }
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircleOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(16.dp)
                )
                Text(
                    text = stringResource(R.string.add_notifications_time),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(Modifier.height(16.dp))
            LazyColumn {
                notificationsPattern.forEach {
                    item {
                        NotificationItem(
                            time = it.first,
                            quantity = it.second,
                            form = med.type,
                            forms = forms
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
        NavigationRow(
            onBack = onBack::invoke,
            onNext = {
                reducer(NewCourseIntent.UpdateUsagesPattern(notificationsPattern))
                onNext()
            }
        )
    }

    if(dialogIsShown) {
        Dialog(onDismissRequest = { dialogIsShown = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                TimeAndTypeSelector(
                    initTime = LocalTime.now().withMinute(0).withSecond(0),
                    onFinish = { dialogIsShown = false },
                    onSelect = { time, quantity ->
                        notificationsPattern = notificationsPattern.toMutableList().apply {
                            add(Pair(time, quantity))
                        }.sortedBy { it.first }.toList()
                    }
                )
            }
        }
    }
}

@Composable
private fun NotificationItem(
    modifier: Modifier = Modifier,
    time: LocalTime,
    quantity: Int,
    form: Int,
    forms: Array<String>
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(Modifier.width(0.dp))
            Text(
                text = time.format(DateTimeFormatter.ofPattern("HH:mm")),
                style = Typography.bodyLarge
            )
            Text(
                text = "$quantity, ${forms[form]}",
                style = Typography.bodyMedium
            )
            Spacer(Modifier.width(0.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TimeAndTypeSelector(
    modifier: Modifier = Modifier,
    initTime: LocalTime,
    initQuantity: Int = 0,
    onSelect: (time: LocalTime, quantity: Int) -> Unit,
    onFinish: () -> Unit
) {
    val list = (1..10).toList()
    val minutes = (0..59).toList()
    val hours = (0..23).toList()
    val pagerStateHours = rememberPagerState(initialPage = minutes.size*10000 + initTime.hour)
    val pagerStateMinutes = rememberPagerState(initialPage = hours.size*10000 + initTime.minute)
    val pagerState = rememberPagerState(initialPage = list.size*10000 + initQuantity)

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
                    val t = if(hours[it % hours.size] < 10) "0${hours[it % hours.size]}" else "${hours[it % hours.size]}"
                    Text( text = t, style = Typography.headlineLarge)
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
                    val t = if(minutes[it % minutes.size] < 10) "0${minutes[it % minutes.size]}" else "${minutes[it % minutes.size]}"
                    Text(text = t, style = Typography.headlineLarge)
                }
            }
            VerticalPager(
                pageCount = Int.MAX_VALUE,
                state = pagerState,
                pageSpacing = 8.dp,
                contentPadding = PaddingValues(top = 84.dp, bottom = 90.dp),
                pageSize = PageSize.Fixed(32.dp),
                modifier = Modifier.height(200.dp)
            ) {
            val ts = when((pagerState.currentPage - it).absoluteValue) {
                0 -> 1f
                1 -> 0.85f
                else -> 0.7f
            }
            val a = when((pagerState.currentPage - it).absoluteValue) {
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
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .width(32.dp)
                ) {
                    Text(
                        text = list[it % list.size].toString(),
                        style = Typography.bodyLarge.copy(fontSize = 16.sp),
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
        }
            Spacer(Modifier.width(0.dp))
        }
        Spacer(Modifier.height(16.dp))
        Button(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            content = { Text(text = stringResource(R.string.add_notifications_time)) },
            onClick = {
                val newTime = initTime
                    .withHour(pagerStateHours.currentPage % hours.size)
                    .withMinute(pagerStateMinutes.currentPage % minutes.size)
                onSelect(newTime, (pagerState.currentPage % list.size) + 1)
            },
        )
        Spacer(Modifier.height(6.dp))
        Button(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            content = { Text(text = stringResource(R.string.settings_save), color = MaterialTheme.colorScheme.onBackground) },
            onClick = onFinish::invoke,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        )
    }
}