package app.mybad.notifier.ui.screens.newcourse.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
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
import app.mybad.notifier.ui.common.ParameterIndicator
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.TitleText
import app.mybad.notifier.ui.screens.newcourse.common.DateDelaySelector
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.screens.newcourse.common.TimeSelector
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import app.mybad.utils.nextCourseStart
import app.mybad.utils.timeInMinutesToDisplay
import app.mybad.utils.toDateTimeDisplay
import kotlinx.datetime.DateTimePeriod

@Composable
@Preview
fun RemindNewCourseBottomSheet(
    modifier: Modifier = Modifier,
    endDate: Long = -1L,
    onSave: (remindDate: Long, interval: Long) -> Unit = { _, _ -> }
) {
    var selectedInput by remember { mutableStateOf(-1) }

    var coursesInterval by remember { mutableStateOf(DateTimePeriod(days = 0)) }
    var remindBeforePeriod by remember { mutableStateOf(DateTimePeriod(days = 0)) }
    var remindTime by remember { mutableStateOf(840) } // 14:00

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
                        value = stringResource(
                            R.string.period_m_d,
                            coursesInterval.months,
                            coursesInterval.days
                        ),
                        onClick = { selectedInput = 1 }
                    )
                },
                {
                    ParameterIndicator(
                        name = stringResource(R.string.add_next_course_remind_before),
                        value = stringResource(
                            R.string.period_m_d,
                            remindBeforePeriod.months,
                            remindBeforePeriod.days
                        ),
                        onClick = { selectedInput = 2 }
                    )
                },
                {
                    ParameterIndicator(
                        name = stringResource(R.string.add_next_course_remind_time),
                        value = remindTime.timeInMinutesToDisplay(),
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
                val (remindDate, interval) = endDate.nextCourseStart(
                    remindTime = remindTime,
                    coursesInterval = coursesInterval,
                    remindBeforePeriod = remindBeforePeriod,
                )
                Log.w(
                    "VTTAG",
                    "RemindNewCourseBottomSheet::updateReminder: endDay=${endDate.toDateTimeDisplay()} remindDate=${remindDate.toDateTimeDisplay()} coursesInterval=${coursesInterval.months}:${coursesInterval.days} interval=$interval"
                )
                onSave(remindDate, interval)
            }
        )
    }

    if (selectedInput != -1) {
        Dialog(onDismissRequest = { selectedInput = -1 }) {
            androidx.compose.material.Surface(
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
                        initTime = remindTime,
                        onSelect = { time ->
                            selectedInput = -1
                            remindTime = time
                        }
                    )
                }
            }
        }
    }
}
