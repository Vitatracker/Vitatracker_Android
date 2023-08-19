package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.mybad.notifier.ui.screens.common.ParameterIndicator
import app.mybad.notifier.ui.screens.newcourse.common.DateDelaySelector
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.screens.newcourse.common.TimeSelector
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.notifier.ui.screens.reuse.TitleText
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.utils.changeTime
import app.mybad.notifier.utils.getCurrentDateTime
import app.mybad.notifier.utils.minus
import app.mybad.notifier.utils.plus
import app.mybad.notifier.utils.toEpochSecond
import app.mybad.notifier.utils.toLocalDateTime
import app.mybad.notifier.utils.toTimeDisplay
import app.mybad.theme.R
import kotlinx.datetime.DateTimePeriod

@Composable
@Preview
fun RemindNewCourseBottomSheet(
    modifier: Modifier = Modifier,
    startDate: Long = -1L,
    onSave: (remindDate: Long, interval: Long) -> Unit = { _: Long, _: Long -> }
) {
    var selectedInput by remember { mutableStateOf(-1) }
    var coursesInterval by remember { mutableStateOf(DateTimePeriod(days = 3)) }
    var remindTime by remember {
        mutableStateOf(getCurrentDateTime().changeTime(14, 0))
    }
    var remindBeforePeriod by remember { mutableStateOf(DateTimePeriod(days = 3)) }

    Column(
        modifier = modifier
            .fillMaxHeight(0.7f)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            TitleText(
                modifier = Modifier.fillMaxWidth(),
                textStringRes = R.string.add_course_reminder,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.add_med_settings),
                style = Typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            MultiBox(
                {
                    ParameterIndicator(
                        name = stringResource(R.string.add_next_course_interval),
                        value = "${coursesInterval.months} m. ${coursesInterval.days} d.",
                        onClick = { selectedInput = 1 }
                    )
                },
                {
                    ParameterIndicator(
                        name = stringResource(R.string.add_next_course_remind_before),
                        value = "${remindBeforePeriod.months} m. ${remindBeforePeriod.days} d.",
                        onClick = { selectedInput = 2 }
                    )
                },
                {
                    ParameterIndicator(
                        name = stringResource(R.string.add_next_course_remind_time),
                        value = remindTime.toTimeDisplay(),
                        onClick = { selectedInput = 3 }
                    )
                },
                itemsPadding = PaddingValues(16.dp)
            )
        }
        ReUseFilledButton(
            modifier = Modifier.fillMaxWidth(),
            textId = R.string.settings_save,
            onClick = {
                val nextCourseStart = startDate.toLocalDateTime().plus(coursesInterval)
                val reminder = nextCourseStart.minus(remindBeforePeriod).changeTime(remindTime)
                onSave(reminder.toEpochSecond(), nextCourseStart.toEpochSecond() - startDate)
            }
        )
    }

    if (selectedInput != -1) {
        Dialog(onDismissRequest = { selectedInput = -1 }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                when (selectedInput) {
                    1 -> DateDelaySelector(
                        initValue = coursesInterval,
                        onSelect = { coursesInterval = it; selectedInput = -1 }
                    )

                    2 -> DateDelaySelector(
                        initValue = remindBeforePeriod,
                        onSelect = { remindBeforePeriod = it; selectedInput = -1 }
                    )

                    3 -> TimeSelector(
                        initTime = remindTime.toEpochSecond(),
                        onSelect = { remindTime = it.toLocalDateTime(); selectedInput = -1 }
                    )
                }
            }
        }
    }
}
