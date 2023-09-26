package app.mybad.notifier.ui.screens.mycoursesedit

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.mybad.data.models.UsageFormat
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.AddNotificationButton
import app.mybad.notifier.ui.common.NotificationItem
import app.mybad.notifier.ui.common.ParameterIndicator
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.common.usagesPatternPreview
import app.mybad.notifier.ui.screens.newcourse.common.DateDelaySelector
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.screens.newcourse.common.TimeSelector
import app.mybad.notifier.ui.screens.newcourse.common.TimeSelectorDialog
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.theme.R
import app.mybad.utils.displayTimeInMinutes
import kotlinx.coroutines.flow.Flow

@Composable
fun MyCourseEditNotificationScreen(
    state: MyCoursesEditContract.State,
    effectFlow: Flow<MyCoursesEditContract.Effect>? = null,
    sendEvent: (event: MyCoursesEditContract.Event) -> Unit = {},
    navigation: (navigationEffect: MyCoursesEditContract.Effect.Navigation) -> Unit = {},
) {
    val forms = stringArrayResource(R.array.types)

    var selectedItem: UsageFormat? by remember { mutableStateOf(null) }
    var selectedInput: Int? by remember { mutableStateOf(null) }

    var remindTime by remember { mutableIntStateOf(state.remindTime) }
    var coursesInterval by remember { mutableStateOf(state.coursesInterval) }
    var remindBeforePeriod by remember { mutableStateOf(state.remindBeforePeriod) }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is MyCoursesEditContract.Effect.Navigation -> navigation(effect)
            }
        }
    }

    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.edit_med_title,
                onBackPressed = { sendEvent(MyCoursesEditContract.Event.ActionBack) }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(0.5f),
            ) {
                Text(
                    text = stringResource(id = R.string.add_notifications_time_set),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(8.dp))
                AddNotificationButton(
                    form = state.remedy.type,
                    forms = forms,
                ) { sendEvent(MyCoursesEditContract.Event.AddUsagesPattern) }
                Spacer(Modifier.height(8.dp))
                LazyColumn(
                    state = rememberLazyListState(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(
                        state.usagesPatternEdit,
                        key = { pattern -> pattern.timeInMinutes }
                    ) { pattern ->
                        Log.w(
                            "VTTAG",
                            "MyCourseEditNotificationScreen::items: pattern=${pattern.timeInMinutes} - ${pattern.quantity}"
                        )
                        NotificationItem(
                            time = pattern.timeInMinutes, // с учетом часового пояса
                            quantity = pattern.quantity,
                            form = state.remedy.type,
                            forms = forms,
                            onDelete = {
                                sendEvent(MyCoursesEditContract.Event.DeleteUsagePattern(pattern))
                            },
                            onDoseChange = {
                                sendEvent(
                                    MyCoursesEditContract.Event.ChangeQuantityUsagePattern(
                                        pattern = pattern,
                                        quantity = it
                                    )
                                )
                            },
                            onTimeClick = { selectedItem = pattern }
                        )
                    }
                }
            }
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.add_med_settings),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
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
                            value = remindTime.displayTimeInMinutes(),
                            onClick = { selectedInput = 3 }
                        )
                    },
                    itemsPadding = PaddingValues(16.dp)
                )
                Spacer(Modifier.height(16.dp))
            }
            ReUseFilledButton(
                modifier = Modifier.fillMaxWidth(),
                textId = R.string.navigation_next,
                isEnabled = state.nextAllowed,
            ) {
                sendEvent(
                    MyCoursesEditContract.Event.NotificationUpdateAndEnd(
                        remindTime = remindTime,
                        coursesInterval = coursesInterval,
                        remindBeforePeriod = remindBeforePeriod,
                    )
                )
            }
        }
    }
    selectedItem?.let {
        TimeSelectorDialog(
            initTime = it.timeInMinutes,
            onDismiss = { selectedItem = null },
        ) { time ->
            sendEvent(
                MyCoursesEditContract.Event.ChangeTimeUsagePattern(
                    pattern = it,
                    time = time
                )
            )
            selectedItem = null
        }
    }

    selectedInput?.let {
        Dialog(onDismissRequest = { selectedInput = null }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.background
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

@Preview
@Composable
fun MyCourseEditNotificationScreenPreview() {
    MyBADTheme {
        MyCourseEditNotificationScreen(
            state = MyCoursesEditContract.State(
                usagesPattern = usagesPatternPreview,
                usagesPatternEdit = usagesPatternPreview,
            )
        )
    }
}
