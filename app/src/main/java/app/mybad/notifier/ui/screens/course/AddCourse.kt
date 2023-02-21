package app.mybad.notifier.ui.screens.course

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.usages.UsageDomainModel
import app.mybad.domain.models.usages.UsagesDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.*
import app.mybad.notifier.ui.theme.Typography

@Composable
@Preview
fun AddCourse(
    modifier: Modifier = Modifier,
    medId: Long = 0L,
    userId: String = "",
    onNext: (Pair<CourseDomainModel, UsagesDomainModel>) -> Unit = {},
    onBack: () -> Unit = {},
) {

    var timesPerDay by remember { mutableStateOf(2) }
    var duration by remember { mutableStateOf(2) }
    var newCourse by remember { mutableStateOf(CourseDomainModel(medId = medId, userId = userId)) }
    var newUsages by remember { mutableStateOf(UsagesDomainModel(medId = medId, userId = userId)) }
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
            Text(
                text = stringResource(id = R.string.add_course_h),
                style = Typography.headlineLarge
            )
            Spacer(Modifier.height(16.dp))
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
                duration = it
            }
            Spacer(Modifier.height(16.dp))
            CommentInput(label = stringResource(R.string.add_course_comment)) {
                newCourse = newCourse.copy(comment = it)
            }
        }
        NavigationRow(
            onBack = onBack::invoke,
            onNext = {
                newUsages = generateUsages(
                    medId = medId,
                    userId = userId,
                    startDate = newCourse.startDate,
                    duration = duration,
                    usagesByDay = newUsagesList
                )
                onNext(Pair(newCourse, newUsages))
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

private fun generateUsages(
    usagesByDay: List<Int>,
    medId: Long,
    userId: String,
    startDate: Long,
    duration: Int,
) : UsagesDomainModel {

    val usagesList = mutableListOf<UsageDomainModel>()
    repeat(duration) { position ->
        usagesByDay.forEach {
            val time = (startDate+position*86400+it)
            usagesList.add(UsageDomainModel(timeToUse = time))
        }
    }
    return UsagesDomainModel(
        medId = medId,
        userId = userId,
        usages = usagesList
    )
}