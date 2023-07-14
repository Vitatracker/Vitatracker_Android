package app.mybad.notifier.ui.screens.newcourse.screens

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
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.theme.R
import app.mybad.notifier.ui.screens.common.CalendarSelectorScreen
import app.mybad.notifier.ui.screens.common.ParameterIndicator
import app.mybad.notifier.ui.screens.newcourse.NewCourseIntent
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.screens.newcourse.common.RollSelector
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.notifier.ui.screens.reuse.TitleText
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.utils.atEndOfDay
import app.mybad.notifier.utils.atStartOfDay
import app.mybad.notifier.utils.toDateFullDisplay
import app.mybad.notifier.utils.toEpochSecond
import app.mybad.notifier.utils.toLocalDateTime
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddCourseMainScreen(
    course: CourseDomainModel,
    onNext: (CourseDomainModel) -> Unit,
    onBackPressed: () -> Unit
) {
    val regimeList = stringArrayResource(R.array.regime)
    val startDate = course.startDate.toLocalDateTime().atStartOfDay()
    val endDate = course.endDate.toLocalDateTime().atEndOfDay()
    var selectedInput by remember { mutableStateOf(-1) }
    val bottomSheetState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        topBar = {
            TopAppBar(
                title = {
                    TitleText(textStringRes = R.string.add_course_h)
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.navigation_back))
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.secondary,
                elevation = 0.dp
            )
        },
        scaffoldState = bottomSheetState,
        sheetPeekHeight = 0.dp,
        sheetGesturesEnabled = false,
        sheetContent = {
            RemindNewCourseBottomSheet(
                modifier = Modifier.padding(16.dp),
                course = course,
                onSave = { scope.launch { bottomSheetState.bottomSheetState.collapse() } },
                onCancel = { scope.launch { bottomSheetState.bottomSheetState.collapse() } },
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
                    onClick = { scope.launch { bottomSheetState.bottomSheetState.expand() } },
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
            ReUseFilledButton(textId = R.string.navigation_next) {
                onNext(course)
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
                            // TODO
                            NewCourseIntent.UpdateCourse(
                                course.copy(
                                    startDate = sd?.atStartOfDay()?.toEpochSecond() ?: 0L,
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
                            // TODO
                            NewCourseIntent.UpdateCourse(
                                course.copy(
                                    endDate = ed?.atStartOfDay()?.toEpochSecond() ?: 0L,
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
                            // TODO
                            NewCourseIntent.UpdateCourse(course.copy(regime = it))
                            selectedInput = -1
                        }
                    )
                }
            }
        }
    }
}
