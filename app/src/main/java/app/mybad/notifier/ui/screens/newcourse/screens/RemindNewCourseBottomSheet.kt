package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.NavigationRow
import app.mybad.notifier.ui.screens.common.ParameterIndicator
import app.mybad.notifier.ui.screens.course.NewCourseIntent
import app.mybad.notifier.ui.screens.newcourse.common.DateDelaySelector
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.screens.newcourse.common.SwitchParameterInput
import app.mybad.notifier.ui.screens.newcourse.common.TimeSelector
import java.time.*
import java.time.format.DateTimeFormatter

@Composable
@Preview
fun RemindNewCourseBottomSheet(
    modifier: Modifier = Modifier,
    course: CourseDomainModel = CourseDomainModel(),
    reducer: (NewCourseIntent) -> Unit = {},
    onSave: () -> Unit = {},
    onCancel: () -> Unit = {}
) {

    val repeatableLabel = stringResource(R.string.add_course_repeatable)
    val interval = stringResource(R.string.add_next_course_interval)
    val remindBefore = stringResource(R.string.add_next_course_remind_before)
    val remindTimeLabel = stringResource(R.string.add_next_course_remind_time)
    val courseStartDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(course.startDate), ZoneId.systemDefault())

    var isRepeatable by remember { mutableStateOf(false) }
    var selectedInput by remember { mutableStateOf(-1) }
    var coursesInterval by remember { mutableStateOf(Period.ofDays(3)) }
    var remindTime by remember { mutableStateOf(LocalTime.of(14, 0)) }
    var remindBeforePeriod by remember { mutableStateOf(Period.ofDays(3)) }

    Column(
        modifier = modifier
            .fillMaxHeight(0.7f)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            MultiBox(
                { SwitchParameterInput(label = repeatableLabel, initValue = isRepeatable, isActive = selectedInput == -1,
                    onSwitch = { isRepeatable = it }) },
                itemsPadding = PaddingValues(16.dp)
            )
            Spacer(Modifier.height(16.dp))
            MultiBox(
                { ParameterIndicator(name = interval, value = "${coursesInterval.months} m. ${coursesInterval.days} d.",
                    onClick = { if(isRepeatable) selectedInput = 1 }) },
                { ParameterIndicator(name = remindBefore, value = "${remindBeforePeriod.months} m. ${remindBeforePeriod.days} d.",
                    onClick = { if(isRepeatable) selectedInput = 2 }) },
                { ParameterIndicator(name = remindTimeLabel, value = remindTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                    onClick = { if(isRepeatable) selectedInput = 3 }) },
                itemsPadding =  PaddingValues(16.dp),
                modifier = Modifier.alpha(if(isRepeatable) 1f else 0.5f)
            )
        }
        NavigationRow(
            backLabel = stringResource(R.string.settings_cancel),
            nextLabel = stringResource(R.string.settings_save),
            onNext = {
                val nextCourseStart = courseStartDate + coursesInterval
                val reminder = (nextCourseStart - remindBeforePeriod).withSecond(0)
                    .withHour(remindTime.hour).withMinute(remindTime.minute)
                onSave()
                reducer(NewCourseIntent.UpdateCourse(course.copy(
                    remindDate = reminder.atZone(ZoneId.systemDefault()).toEpochSecond(),
                    interval = nextCourseStart.atZone(ZoneId.systemDefault()).toEpochSecond() - course.startDate,
                )))
            },
            onBack = onCancel::invoke
        )
    }

    if(selectedInput != -1 && isRepeatable) {
        Dialog(onDismissRequest = { selectedInput = -1 }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                when(selectedInput) {
                    1 -> DateDelaySelector(
                        initValue = coursesInterval,
                        onSelect = { coursesInterval = it; selectedInput = -1 }
                    )
                    2 -> DateDelaySelector(
                        initValue = remindBeforePeriod,
                        onSelect = { remindBeforePeriod = it; selectedInput = -1 }
                    )
                    3 -> TimeSelector(
                        initTime = remindTime,
                        onSelect = { remindTime = it; selectedInput = -1 }
                    )
                }
            }
        }
    }
}