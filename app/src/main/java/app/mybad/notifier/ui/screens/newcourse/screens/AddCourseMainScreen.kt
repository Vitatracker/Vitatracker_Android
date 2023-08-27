package app.mybad.notifier.ui.screens.newcourse.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import app.mybad.notifier.ui.screens.common.ParameterIndicator
import app.mybad.notifier.ui.screens.common.SingleDayCalendarSelector
import app.mybad.notifier.ui.screens.newcourse.CreateCourseScreensContract
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.screens.newcourse.common.RollSelector
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.notifier.ui.screens.reuse.TopAppBarWithBackAction
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.utils.atEndOfDay
import app.mybad.notifier.utils.atStartOfDay
import app.mybad.notifier.utils.toDateFullDisplay
import app.mybad.notifier.utils.toEpochSecond
import app.mybad.notifier.utils.toLocalDateTime
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseMainScreen(
    state: CreateCourseScreensContract.State,
    events: Flow<CreateCourseScreensContract.Effect>? = null,
    onEventSent: (event: CreateCourseScreensContract.Event) -> Unit = {},
    onNavigationRequested: (navigationEffect: CreateCourseScreensContract.Effect.Navigation) -> Unit
) {
    val regimeList = stringArrayResource(R.array.regime)
    val startDate = state.course.startDate.toLocalDateTime().atStartOfDay()
    val endDate = state.course.endDate.toLocalDateTime().atEndOfDay()
    var selectedInput by remember { mutableStateOf(-1) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        events?.collect {
            when (it) {
                CreateCourseScreensContract.Effect.Navigation.ActionBack -> {
                    onNavigationRequested(CreateCourseScreensContract.Effect.Navigation.ActionBack)
                }

                CreateCourseScreensContract.Effect.Navigation.ActionNext -> {
                    onNavigationRequested(CreateCourseScreensContract.Effect.Navigation.ActionNext)
                }
            }
        }
    }
    if (bottomSheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = { scope.launch { bottomSheetState.hide() } },
            sheetState = bottomSheetState,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            RemindNewCourseBottomSheet(
                modifier = Modifier.padding(16.dp),
                startDate = state.course.startDate,
                onSave = { remindDate, interval ->
                    val newCourse = state.course.copy(remindDate = remindDate, interval = interval)
                    onEventSent(CreateCourseScreensContract.Event.UpdateCourse(newCourse))
                    onEventSent(CreateCourseScreensContract.Event.CourseIntervalEntered)
                    scope.launch { bottomSheetState.hide() }
                }
            )
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
    Scaffold(
        topBar = {
            TopAppBarWithBackAction(
                titleResId = R.string.add_course_h,
                onBackPressed = { onEventSent(CreateCourseScreensContract.Event.ActionBack) }
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.mycourse_duration),
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                MultiBox(
                    {
                        ParameterIndicator(
                            name = stringResource(R.string.add_course_start_time),
                            value = startDate.toDateFullDisplay(),
                            onClick = { selectedInput = 1 }
                        )
                    },
                    {
                        ParameterIndicator(
                            name = stringResource(R.string.add_course_end_time),
                            value = endDate.toDateFullDisplay(),
                            onClick = { selectedInput = 2 }
                        )
                    },
                    {
                        ParameterIndicator(
                            name = stringResource(R.string.medication_regime),
                            value = regimeList[state.course.regime],
                            onClick = { selectedInput = 3 }
                        )
                    },
                    itemsPadding = PaddingValues(16.dp)
                )
                Spacer(Modifier.height(32.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.background),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                    onClick = {
                        if (!state.courseIntervalEntered) {
                            scope.launch { bottomSheetState.expand() }
                        }
                    },
                    contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
                ) {
                    var icon = R.drawable.clock
                    var buttonText = R.string.add_course_reminder
                    if (state.courseIntervalEntered) {
                        icon = R.drawable.done
                        buttonText = R.string.add_course_reminder_completed
                    }
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(buttonText),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            ReUseFilledButton(
                modifier = Modifier.fillMaxWidth(),
                textId = R.string.navigation_next
            ) {
                Log.d("VTTAG", "new course ${state.course}")
                Log.d("VTTAG", "new usages ${state.usages}")
                Log.d("VTTAG", "new med ${state.med}")
                onEventSent(CreateCourseScreensContract.Event.ActionNext)
            }
        }
    }

    if (selectedInput != -1) {
        when (selectedInput) {
            1 -> {
                DatePickerDialog(
                    initDate = startDate,
                    onDismissRequest = { selectedInput = -1 },
                    onDatePicked = { newDate ->
                        val newCourse = state.course.copy(startDate = newDate?.atStartOfDay()?.toEpochSecond() ?: 0L)
                        onEventSent(CreateCourseScreensContract.Event.UpdateCourse(newCourse))
                    }
                )
            }

            2 -> {
                DatePickerDialog(
                    initDate = endDate,
                    onDismissRequest = { selectedInput = -1 },
                    onDatePicked = { newDate ->
                        val newCourse = state.course.copy(endDate = newDate?.atStartOfDay()?.toEpochSecond() ?: 0L)
                        onEventSent(CreateCourseScreensContract.Event.UpdateCourse(newCourse))
                    }
                )
            }

            3 -> {
                RegimeSelector(
                    regimeList = regimeList.toList(),
                    startOffset = state.course.regime,
                    onDismissRequest = {
                        selectedInput = -1
                    },
                    onRegimePicked = { newRegime ->
                        val newCourse = state.course.copy(regime = newRegime)
                        onEventSent(CreateCourseScreensContract.Event.UpdateCourse(newCourse))
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun DatePickerDialogPreview() {
    DatePickerDialog(
        initDate = LocalDateTime(2023, 5, 4, 5, 5, 5),
        onDismissRequest = {},
        onDatePicked = {}
    )
}

@Composable
fun DatePickerDialog(
    initDate: LocalDateTime,
    onDismissRequest: () -> Unit = {},
    onDatePicked: (event: LocalDateTime?) -> Unit = {}
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.background,
        ) {
            SingleDayCalendarSelector(
                initDay = initDate,
                onSelect = {
                    onDatePicked(it)
                    onDismissRequest()
                }
            )
        }
    }
}

@Preview
@Composable
fun RegimeSelector(
    regimeList: List<String> = listOf("Regime 1, Regime 2, Regime 3"),
    startOffset: Int = -1,
    onDismissRequest: () -> Unit = {},
    onRegimePicked: (Int) -> Unit = {}
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.background,
        ) {
            RollSelector(
                list = regimeList,
                startOffset = startOffset,
                onSelect = {
                    onRegimePicked(it)
                    onDismissRequest()
                }
            )
        }
    }
}