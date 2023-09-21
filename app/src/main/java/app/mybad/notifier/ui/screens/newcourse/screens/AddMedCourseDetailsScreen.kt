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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
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
import app.mybad.data.models.CourseSelectInput
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.CalendarAndRegimeSelectorDialog
import app.mybad.notifier.ui.common.ParameterIndicator
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.screens.newcourse.CreateCourseContract
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atStartOfDay
import app.mybad.utils.toDateFullDisplay
import app.mybad.utils.toEpochSecond
import app.mybad.utils.toLocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedCourseDetailsScreen(
    state: CreateCourseContract.State,
    effectFlow: Flow<CreateCourseContract.Effect>? = null,
    sendEvent: (event: CreateCourseContract.Event) -> Unit = {},
    navigation: (navigationEffect: CreateCourseContract.Effect.Navigation) -> Unit = {},
) {
    Log.w("VTTAG", "AddMedCourseDetailsScreen:: start")
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        // обновим начальную дату курса и пределы дат
        sendEvent(CreateCourseContract.Event.UpdateCourseStartDateAndLimit)
        effectFlow?.collect { effect ->
            when (effect) {
                is CreateCourseContract.Effect.Navigation -> navigation(effect)
                is CreateCourseContract.Effect.Collapse -> {}
                is CreateCourseContract.Effect.Expand -> {}
            }
        }
    }
    val regimeList = stringArrayResource(R.array.regime)
    val startDate = state.course.startDate.toLocalDateTime().atStartOfDay()
    val endDate = state.course.endDate.toLocalDateTime().atEndOfDay()
    var selectedInput by remember { mutableStateOf<CourseSelectInput?>(null) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    if (bottomSheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = { scope.launch { bottomSheetState.hide() } },
            sheetState = bottomSheetState,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            RemindNewCourseBottomSheet(
                modifier = Modifier.padding(16.dp),
                endDate = state.course.endDate,
                onSave = { remindDate, interval ->
                    val newCourse = state.course.copy(remindDate = remindDate, interval = interval)
                    sendEvent(CreateCourseContract.Event.UpdateCourse(newCourse))
                    sendEvent(CreateCourseContract.Event.CourseIntervalEntered)
                    scope.launch { bottomSheetState.hide() }
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.add_course_title,
                onBackPressed = { sendEvent(CreateCourseContract.Event.ActionBack) }
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
                            onClick = { selectedInput = CourseSelectInput.SELECT_START_DATE }
                        )
                    },
                    {
                        ParameterIndicator(
                            name = stringResource(R.string.add_course_end_time),
                            value = endDate.toDateFullDisplay(),
                            onClick = { selectedInput = CourseSelectInput.SELECT_END_DATE }
                        )
                    },
                    {
                        ParameterIndicator(
                            name = stringResource(R.string.medication_regime),
                            value = regimeList[state.course.regime],
                            onClick = { selectedInput = CourseSelectInput.SELECT_REGIME }
                        )
                    },
                    itemsPadding = PaddingValues(16.dp)
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    onClick = {
                        if (!state.courseIntervalEntered) {
                            scope.launch { bottomSheetState.expand() }
                        }
                    },
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 20.dp,
                        bottom = 20.dp
                    )
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
                            .padding(end = 16.dp)
                            .size(24.dp)
                    )
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
                sendEvent(CreateCourseContract.Event.ActionNext)
            }
        }
    }

    selectedInput?.let { select ->
        CalendarAndRegimeSelectorDialog(
            selectedInput = select,
            startDate = startDate,
            endDate = endDate,
            regime = state.course.regime,
            regimeList = regimeList.toList(),
            onDismissRequest = { selectedInput = null },
            onDateSelected = {
                val newCourse = state.course.copy(
                    startDate = it.first.toEpochSecond(),
                    endDate = it.second.toEpochSecond(),
                )
                sendEvent(CreateCourseContract.Event.UpdateCourse(newCourse))
            },
            onRegimeSelected = {
                val newCourse = state.course.copy(regime = it)
                sendEvent(CreateCourseContract.Event.UpdateCourse(newCourse))
            }
        )
    }
}

@Preview
@Composable
fun AddMedCourseDetailsScreenPreview() {
    MyBADTheme {
        AddMedCourseDetailsScreen(
            state = CreateCourseContract.State(),
        )
    }
}
