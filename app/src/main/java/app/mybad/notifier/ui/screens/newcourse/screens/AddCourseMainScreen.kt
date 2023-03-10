package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.NavigationRow
import app.mybad.notifier.ui.screens.common.ParameterIndicator
import app.mybad.notifier.ui.screens.course.NewCourseIntent
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.screens.newcourse.common.RollSelector
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun AddCourseMainScreen(
    modifier: Modifier = Modifier,
    course: CourseDomainModel,
    reducer: (NewCourseIntent) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {

    val startLabel = stringResource(R.string.add_course_start_time)
    val endLabel = stringResource(R.string.add_course_end_time)
    val regimeLabel = stringResource(R.string.medication_regime)
    val regimeList = stringArrayResource(R.array.regime)
    val startDate = LocalDateTime.ofEpochSecond(course.startDate, 0, ZoneOffset.UTC)
    val endDate = LocalDateTime.ofEpochSecond(course.endDate, 0, ZoneOffset.UTC)
    var selectedInput by remember { mutableStateOf(-1) }
    val sState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = sState,
        sheetPeekHeight = 0.dp,
        sheetContent = { RemindNewCourseBottomSheet(
            modifier = Modifier.padding(16.dp),
            course = course,
            reducer = reducer,
            onSave = { scope.launch { sState.bottomSheetState.collapse() }},
            onCancel = { scope.launch { sState.bottomSheetState.collapse() }},
        ) }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxSize()
        ) {
            Column {
                MultiBox(
                    { ParameterIndicator(name = startLabel, value = startDate,
                        onClick = { selectedInput = 1 } ) },
                    { ParameterIndicator(name = endLabel, value = endDate,
                        onClick = { selectedInput = 2 } ) },
                    { ParameterIndicator(name = regimeLabel, value = regimeList[course.regime],
                        onClick = { selectedInput = 3 } ) },
                    itemsPadding = PaddingValues(16.dp)
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.background),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    onClick = { scope.launch { sState.bottomSheetState.expand() }},
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.clock),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(24.dp)
                    )
                    Text(text = stringResource(R.string.add_course_reminder))
                }
            }
            NavigationRow(
                onNext = onNext::invoke,
                onBack = onBack::invoke
            )
        }
    }

    if(selectedInput != -1) {
        Dialog(
            onDismissRequest = { selectedInput = -1 },
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                val state = rememberDatePickerState()
                when(selectedInput) {
                    1, 2 ->
                        Column(modifier = Modifier.padding(16.dp)) {
                            DatePicker(state = state)
                            NavigationRow(
                                backLabel = stringResource(R.string.settings_cancel),
                                nextLabel = stringResource(R.string.settings_save),
                                onBack = { selectedInput = -1 },
                                onNext = {
                                    if(selectedInput == 1) {
                                        reducer(NewCourseIntent.UpdateCourse(course.copy(
                                            startDate = (state.selectedDateMillis ?: 0L)/1000 )))
                                    } else {
                                        reducer(NewCourseIntent.UpdateCourse(course.copy(
                                            endDate = (state.selectedDateMillis ?: 0L)/1000 )))
                                    }
                                    selectedInput = -1
                                }
                            )
                        }
                    3 -> RollSelector(
                        list = regimeList.toList(),
                        startOffset = course.regime,
                        onSelect = {
                            reducer(NewCourseIntent.UpdateCourse(course.copy(regime = it)))
                            selectedInput = -1
                        }
                    )
                }
            }
        }
    }
}