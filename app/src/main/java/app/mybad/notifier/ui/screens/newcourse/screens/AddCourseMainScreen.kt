package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.window.DialogProperties
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.CalendarSelectorScreen
import app.mybad.notifier.ui.screens.common.ParameterIndicator
import app.mybad.notifier.ui.screens.newcourse.NewCourseIntent
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.screens.newcourse.common.RollSelector
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.utils.atEndOfDay
import app.mybad.notifier.utils.atStartOfDay
import app.mybad.notifier.utils.getCurrentDateTime
import app.mybad.notifier.utils.toDateFullDisplay
import app.mybad.notifier.utils.toEpochSecond
import app.mybad.notifier.utils.toLocalDateTime
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddCourseMainScreen(
    modifier: Modifier = Modifier,
    course: CourseDomainModel,
    reducer: (NewCourseIntent) -> Unit,
    onNext: () -> Unit,
) {
    val startLabel = stringResource(R.string.add_course_start_time)
    val endLabel = stringResource(R.string.add_course_end_time)
    val regimeLabel = stringResource(R.string.medication_regime)
    val regimeList = stringArrayResource(R.array.regime)
    val startDate = course.startDate.toLocalDateTime().atStartOfDay()
    val endDate = course.endDate.toLocalDateTime().atEndOfDay()
    var selectedInput by remember { mutableStateOf(-1) }
    val sState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = sState,
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = false,
        sheetContent = {
            RemindNewCourseBottomSheet(
                modifier = Modifier.padding(16.dp),
                course = course,
                reducer = reducer,
                onSave = { scope.launch { sState.bottomSheetState.collapse() } },
                onCancel = { scope.launch { sState.bottomSheetState.collapse() } },
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxSize()
        ) {
            Column {
                MultiBox(
                    {
                        ParameterIndicator(
                            name = startLabel,
                            value = startDate.toDateFullDisplay(),
                            onClick = { selectedInput = 1 }
                        )
                    },
                    {
                        ParameterIndicator(
                            name = endLabel,
                            value = endDate.toDateFullDisplay(),
                            onClick = { selectedInput = 2 }
                        )
                    },
                    {
                        ParameterIndicator(
                            name = regimeLabel,
                            value = regimeList[course.regime],
                            onClick = { selectedInput = 3 }
                        )
                    },
                    itemsPadding = PaddingValues(16.dp)
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.background),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    onClick = { scope.launch { sState.bottomSheetState.expand() } },
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
            androidx.compose.material3.Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                shape = RoundedCornerShape(10.dp),
                onClick = onNext::invoke
            ) {
                Text(
                    text = stringResource(R.string.navigation_next),
                    style = Typography.bodyLarge
                )
            }
        }
    }

    if (selectedInput != -1) {
        Dialog(
            onDismissRequest = { selectedInput = -1 },
            properties = DialogProperties()
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.background,
            ) {
                when (selectedInput) {
                    1 -> CalendarSelectorScreen(
                        startDay = startDate,
                        endDay = endDate,
                        onSelect = { sd ->
                            reducer(
                                NewCourseIntent.UpdateCourse(
                                    course.copy(
                                        startDate = sd?.atStartOfDay()?.toEpochSecond() ?: 0L,
                                    )
                                )
                            )
                            selectedInput = -1
                        },
                        onDismiss = { selectedInput = -1 },
                        editStart = true
                    )

                    2 -> CalendarSelectorScreen(
                        startDay = startDate,
                        endDay = endDate,
                        onSelect = { ed ->
                            reducer(
                                NewCourseIntent.UpdateCourse(
                                    course.copy(
                                        endDate = ed?.atStartOfDay()?.toEpochSecond() ?: 0L,
                                    )
                                )
                            )
                            selectedInput = -1
                        },
                        onDismiss = { selectedInput = -1 },
                        editStart = false
                    )

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
