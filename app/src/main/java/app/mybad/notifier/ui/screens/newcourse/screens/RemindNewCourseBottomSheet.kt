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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.mybad.notifier.ui.common.ParameterIndicator
import app.mybad.notifier.ui.common.ReUseAnimatedVisibility
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.TitleText
import app.mybad.notifier.ui.screens.newcourse.common.DateDelaySelector
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.screens.newcourse.common.TimeSelector
import app.mybad.theme.R
import app.mybad.utils.days
import app.mybad.utils.displayDateTime
import app.mybad.utils.displayTimeInMinutes
import app.mybad.utils.months
import app.mybad.utils.nextCourseIntervals
import app.mybad.utils.nextCourseStart
import kotlinx.datetime.LocalDateTime

@Composable
fun RemindNewCourseBottomSheet(
    modifier: Modifier = Modifier,
    endDate: LocalDateTime,
    remindDate: LocalDateTime?,
    interval: Long,
    onSave: (remindDate: LocalDateTime?, interval: Long) -> Unit = { _, _ -> }
) {
    var selectedInput: Int? by remember { mutableStateOf(null) }

    // remindTime, coursesInterval, remindBeforePeriod
    val remindTimeInit by remember(endDate, remindDate, interval) {
        mutableStateOf(
            endDate.nextCourseIntervals(
                remindDate,
                interval
            )
        )
    }
    var remindTime by remember(remindTimeInit) { mutableIntStateOf(remindTimeInit.first) }
    var coursesInterval by remember(remindTimeInit) { mutableIntStateOf(remindTimeInit.second) }
    var remindBeforePeriod by remember(remindTimeInit) { mutableIntStateOf(remindTimeInit.third) }
    Log.w(
        "VTTAG",
        "RemindNewCourseBottomSheet::RemindNewCourseBottomSheet: endDate=${
            endDate.displayDateTime()
        } interval=$coursesInterval remindDate=${
            remindDate?.displayDateTime()
        } remindTime=${
            remindTime.displayTimeInMinutes()
        } beforeDay=$remindBeforePeriod"
    )

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
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            MultiBox(
                {
                    ParameterIndicator(
                        name = stringResource(R.string.add_next_course_interval),
                        value = stringResource(
                            R.string.period_m_d,
                            coursesInterval.months(),
                            coursesInterval.days(),
                        ),
                        onClick = { selectedInput = 1 }
                    )
                },
                {
                    ParameterIndicator(
                        name = stringResource(R.string.add_next_course_remind_before),
                        value = stringResource(
                            R.string.period_m_d,
                            remindBeforePeriod.months(),
                            remindBeforePeriod.days(),
                        ),
                        onClick = { selectedInput = 2 }
                    )
                },
                {
                    ParameterIndicator(
                        name = stringResource(R.string.add_next_course_remind_time),
                        value = remindTime.displayTimeInMinutes(),
                        onClick = { selectedInput = 3 }
                    )
                },
                itemsPadding = PaddingValues(16.dp)
            )
        }
        ReUseFilledButton(
            textId = R.string.settings_save,
            onClick = {
                val (remindDateNew, intervalNew) = endDate.nextCourseStart(
                    remindTime = remindTime,
                    coursesInterval = coursesInterval,
                    remindBeforePeriod = remindBeforePeriod,
                )
                Log.w(
                    "VTTAG",
                    "RemindNewCourseBottomSheet::updateReminder: endDay=${endDate.displayDateTime()} remindDate=${remindDateNew?.displayDateTime()} coursesInterval=${coursesInterval.months()}:${coursesInterval.days()} interval=$intervalNew"
                )
                onSave(remindDateNew, intervalNew)
            }
        )
    }

    ReUseAnimatedVisibility(visible = selectedInput != null) {
        Dialog(onDismissRequest = { selectedInput = null }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                when (selectedInput) {
                    1 -> DateDelaySelector(
                        initValue = coursesInterval,
                        onSelect = {
                            coursesInterval = it
                            selectedInput = null
                        }
                    )

                    2 -> DateDelaySelector(
                        initValue = remindBeforePeriod,
                        onSelect = {
                            remindBeforePeriod = it
                            selectedInput = null
                        }
                    )

                    3 -> TimeSelector(
                        initTime = remindTime,
                        onSelect = { time ->
                            remindTime = time
                            selectedInput = null
                        }
                    )
                }
            }
        }
    }
}
