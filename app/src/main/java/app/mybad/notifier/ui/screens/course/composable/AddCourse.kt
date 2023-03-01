package app.mybad.notifier.ui.screens.course.composable

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.*
import app.mybad.notifier.ui.theme.Typography
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Composable
fun AddCourse(
    modifier: Modifier = Modifier,
    medId: Long = 0L,
    userId: String = "",
    onNext: (Pair<CourseDomainModel, List<UsageCommonDomainModel>>) -> Unit = {},
    onBack: () -> Unit = {},
) {

    val now = LocalDateTime.now()
    val startDate = now.withHour(0).withMinute(0).withSecond(0)
        .atZone(ZoneId.systemDefault()).toEpochSecond()
    var timesPerDay by remember { mutableStateOf(2) }
    var duration by remember { mutableStateOf(2) }
    var newCourse by remember { mutableStateOf(CourseDomainModel(
        id = now.toEpochSecond(ZoneOffset.UTC),
        medId = medId,
        userId = userId,
        startDate = startDate,
        creationDate = now.atZone(ZoneId.systemDefault()).toEpochSecond(),
    )) }
    var newCommonUsages by remember {
        mutableStateOf(listOf<UsageCommonDomainModel>())
    }
    var newUsagesList by remember { mutableStateOf(emptyList<Int>()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            IterationsSelector(
                label = stringResource(R.string.add_course_notifications_quantity)
            ) { timesPerDay = it }
            Spacer(Modifier.height(16.dp))
            NotificationsSelector(timesPerDay) {
                newUsagesList = it
            }
            Spacer(Modifier.height(16.dp))
            DateSelector(
                label = stringResource(R.string.add_course_start_time)
            ) {
                newCourse = newCourse.copy(startDate = it)
            }
            Spacer(Modifier.height(16.dp))
            IterationsSelector(
                label = stringResource(R.string.add_course_duration),
                limit = Int.MAX_VALUE,
                readOnly = false
            ) {
                newCourse = newCourse.copy(endDate = newCourse.startDate + it*86400L)
                duration = it
            }
            Spacer(Modifier.height(16.dp))
            TextInput(label = stringResource(R.string.add_course_comment)) {
                newCourse = newCourse.copy(comment = it)
            }
        }
        NavigationRow(
            onBack = onBack::invoke,
            onNext = {
                newCommonUsages = generateCommonUsages(
                    now = now.atZone(ZoneId.systemDefault()).toEpochSecond(),
                    medId = medId,
                    userId = userId,
                    startDate = newCourse.startDate,
                    duration = duration,
                    usagesByDay = newUsagesList
                )
                onNext(Pair(newCourse, newCommonUsages))
            }
        )
    }
}
@Composable
private fun NotificationsSelector(
    quantity: Int,
    onSelect: (List<Int>) -> Unit
) {
    val maxHeight = LocalConfiguration.current.screenHeightDp
    val notifications = mutableListOf<Int>().apply {
        clear()
        repeat(quantity) { add(0) }
    }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = maxHeight.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.add_course_notifications_time),
                style = Typography.bodyLarge
            )
            Spacer(Modifier.height(4.dp))
        }
        items(quantity) { index ->
            TimeSelector(initTime = 3601 + index * 3600, onSelect = {
                notifications[index] = it
                onSelect(notifications)
            })
            Spacer(Modifier.height(8.dp))
        }
    }
}

private fun generateCommonUsages(
    usagesByDay: List<Int>,
    now: Long,
    medId: Long,
    userId: String,
    startDate: Long,
    duration: Int,
) : List<UsageCommonDomainModel> {
    return mutableListOf<UsageCommonDomainModel>().apply {
        repeat(duration) { position ->
            usagesByDay.forEach {
                val time = (startDate+position*86400+it)
                this.add(
                    UsageCommonDomainModel(
                    medId = medId,
                    userId = userId,
                    creationTime = now,
                    useTime = time
                )
                )
            }
        }
    }
}