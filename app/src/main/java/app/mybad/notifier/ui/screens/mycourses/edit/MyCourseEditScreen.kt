package app.mybad.notifier.ui.screens.mycourses.edit

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.mybad.data.models.CourseSelectInput
import app.mybad.data.models.EditCourseState
import app.mybad.data.models.UsageFormat
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.CalendarSelectorScreen
import app.mybad.notifier.ui.common.ParameterIndicator
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.screens.mycourses.MyCoursesContract
import app.mybad.notifier.ui.screens.newcourse.common.BasicKeyboardInput
import app.mybad.notifier.ui.screens.newcourse.common.ColorSelector
import app.mybad.notifier.ui.screens.newcourse.common.IconSelector
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.screens.newcourse.common.RollSelector
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import app.mybad.utils.atEndOfDay
import app.mybad.utils.atEndOfDaySystemToUTC
import app.mybad.utils.atStartOfDay
import app.mybad.utils.atStartOfDaySystemToUTC
import app.mybad.utils.toDateFullDisplay
import app.mybad.utils.toEpochSecond
import kotlinx.coroutines.flow.Flow

private val usagesPattern = listOf(
    UsageFormat(480, 1), //08:00
    UsageFormat(720, 3), //12:00
    UsageFormat(1080, 2),//18:00
)

@Preview
@Composable
fun MyCourseEditScreenPreview() {
    MyBADTheme {
        MyCourseEditScreen(
            MyCoursesContract.State(
                editCourse = EditCourseState(
                    usagesPattern = usagesPattern,
                )
            )
        )
    }
}

@Composable
fun MyCourseEditScreen(
    state: MyCoursesContract.State,
    effectFlow: Flow<MyCoursesContract.Effect>? = null,
    sendEvent: (event: MyCoursesContract.Event) -> Unit = {},
    navigation: (navigationEffect: MyCoursesContract.Effect.Navigation) -> Unit = {},
) {
    val types = stringArrayResource(R.array.types)
    val units = stringArrayResource(R.array.units)
    val rels = stringArrayResource(R.array.food_relations)

    val name = stringResource(R.string.add_med_name)
    val form = stringResource(R.string.add_med_form)
    val dose = stringResource(R.string.mycourse_dosage_and_usage)
    val unit = stringResource(R.string.add_med_unit)
    val rel = stringResource(R.string.add_med_food_relation)

    val startLabel = stringResource(R.string.add_course_start_time)
    val endLabel = stringResource(R.string.add_course_end_time)
    val regimeLabel = stringResource(R.string.medication_regime)
    val regimeList = stringArrayResource(R.array.regime)

    var remedyInternal by remember { mutableStateOf(state.editCourse.remedy) }
    var courseInternal by remember { mutableStateOf(state.editCourse.course) }
    var patternInternal by remember { mutableStateOf(state.editCourse.usagesPattern) }

    var startDate by remember { mutableStateOf(courseInternal.startDate.atStartOfDaySystemToUTC()) }
    var endDate by remember { mutableStateOf(courseInternal.endDate.atEndOfDaySystemToUTC()) }

    var selectedInput: CourseSelectInput? by remember { mutableStateOf(null) }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is MyCoursesContract.Effect.Navigation -> navigation(effect)
            }
        }
    }

    LaunchedEffect(state.editCourse.course.id) {
        remedyInternal = state.editCourse.remedy
        courseInternal = state.editCourse.course
        patternInternal = state.editCourse.usagesPattern
    }

    LaunchedEffect(courseInternal) {
        startDate = courseInternal.startDate.atStartOfDaySystemToUTC()
        endDate = courseInternal.endDate.atEndOfDaySystemToUTC()
    }

    Log.w(
        "VTTAG",
        "CourseInfoScreen::EditCourse: id=${state.editCourse.remedy.id} remedy=${remedyInternal.name} courseId=${
            courseInternal.id
        } usages=${patternInternal.size}"
    )

    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.edit_med_title,
                onBackPressed = { sendEvent(MyCoursesContract.Event.ActionBack) }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringResource(R.string.mycourse_edit_icon),
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                MultiBox(
                    {
                        IconSelector(
                            selected = remedyInternal.icon,
                            color = remedyInternal.color,
                            onSelect = { remedyInternal = remedyInternal.copy(icon = it) }
                        )
                    },
                    itemsPadding = PaddingValues(16.dp),
                    outlineColor = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = stringResource(R.string.mycourse_edit_icon_color),
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                MultiBox(
                    {
                        ColorSelector(
                            selected = remedyInternal.color,
                            onSelect = { remedyInternal = remedyInternal.copy(color = it) }
                        )
                    },
                    itemsPadding = PaddingValues(16.dp),
                    outlineColor = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = stringResource(R.string.mycourse_dosage_and_usage),
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                MultiBox(
                    {
                        BasicKeyboardInput(
                            label = name,
                            init = remedyInternal.name,
                            style = Typography.bodyLarge,
                            hideOnGo = true,
                            onChange = { remedyInternal = remedyInternal.copy(name = it) }
                        )
                    },
                    {
                        var exp by remember { mutableStateOf(false) }
                        ParameterIndicator(
                            name = form,
                            value = types[state.editCourse.remedy.type],
                            onClick = { exp = true }
                        )
                        DropdownMenu(
                            expanded = exp,
                            onDismissRequest = { exp = false },
                            offset = DpOffset(x = 300.dp, y = 0.dp)
                        ) {
                            types.forEachIndexed { index, item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        remedyInternal = remedyInternal.copy(type = index)
                                        exp = false
                                    }
                                )
                            }
                        }
                    },
                    {
                        BasicKeyboardInput(
                            label = dose,
                            init = if (remedyInternal.dose == 0) "" else remedyInternal.dose.toString(),
                            hideOnGo = true,
                            keyboardType = KeyboardType.Number,
                            alignRight = true,
                            prefix = {
                                Text(
                                    text = dose,
                                    style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            },
                            onChange = {
                                remedyInternal = remedyInternal.copy(dose = it.toIntOrNull() ?: 0)
                            }
                        )
                    },
                    {
                        var exp by remember { mutableStateOf(false) }
                        ParameterIndicator(
                            name = unit,
                            value = units[remedyInternal.measureUnit],
                            onClick = { exp = true }
                        )
                        DropdownMenu(
                            expanded = exp,
                            onDismissRequest = { exp = false },
                            offset = DpOffset(x = 300.dp, y = 0.dp)
                        ) {
                            units.forEachIndexed { index, item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        remedyInternal = remedyInternal.copy(measureUnit = index)
                                        exp = false
                                    }
                                )
                            }
                        }
                    },
                    {
                        var exp by remember { mutableStateOf(false) }
                        ParameterIndicator(
                            name = rel,
                            value = rels[remedyInternal.beforeFood],
                            onClick = { exp = true }
                        )
                        DropdownMenu(
                            expanded = exp,
                            onDismissRequest = { exp = false },
                            offset = DpOffset(x = 300.dp, y = 0.dp)
                        ) {
                            rels.forEachIndexed { index, item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        remedyInternal = remedyInternal.copy(beforeFood = index)
                                        exp = false
                                    }
                                )
                            }
                        }
                    },
                    modifier = Modifier,
                    itemsPadding = PaddingValues(16.dp),
                    outlineColor = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = stringResource(R.string.mycourse_duration),
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                MultiBox(
                    {
                        ParameterIndicator(
                            name = startLabel,
                            value = startDate.toDateFullDisplay(),
                            onClick = { selectedInput = CourseSelectInput.SELECT_START_DATE }
                        )
                    },
                    {
                        ParameterIndicator(
                            name = endLabel,
                            value = endDate.toDateFullDisplay(),
                            onClick = { selectedInput = CourseSelectInput.SELECT_END_DATE }
                        )
                    },
                    {
                        ParameterIndicator(
                            name = regimeLabel,
                            value = regimeList[courseInternal.regime],
                            onClick = { selectedInput = CourseSelectInput.SELECT_REGIME }
                        )
                    },
                    itemsPadding = PaddingValues(16.dp)
                )

                Text(
                    text = stringResource(R.string.mycourse_reminders),
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                MultiBox(
                    {
                        Text(
                            text = stringResource(R.string.add_notifications_time_set),
                            modifier = Modifier.clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = { }
                            )
                        )
                    },
                    itemsPadding = PaddingValues(16.dp),
                    outlineColor = MaterialTheme.colorScheme.primary,
                )
            }
            Spacer(Modifier.height(16.dp))
            SaveDecline(
                onSave = {
                    sendEvent(
                        MyCoursesContract.Event.Update(
                            courseInternal,
                            remedyInternal,
                            patternInternal
                        )
                    )
                },
                onDelete = {
                    sendEvent(MyCoursesContract.Event.Delete(courseInternal.id))
                },
            )
        }

        selectedInput?.let { selected ->
            Dialog(
                onDismissRequest = { selectedInput = null },
                properties = DialogProperties()
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    when (selected) {
                        CourseSelectInput.SELECT_START_DATE -> CalendarSelectorScreen(
                            startDay = startDate,
                            endDay = endDate,
                            editStart = true,
                        ) { selectDate ->
                            courseInternal = courseInternal.copy(
                                startDate = selectDate?.atStartOfDay()?.toEpochSecond() ?: 0L,
                            )
                            selectedInput = null
                        }

                        CourseSelectInput.SELECT_END_DATE -> CalendarSelectorScreen(
                            startDay = startDate,
                            endDay = endDate,
                            editStart = false,
                        ) { editDate ->
                            courseInternal = courseInternal.copy(
                                endDate = editDate?.atEndOfDay()?.toEpochSecond() ?: 0L,
                            )
                            selectedInput = null
                        }

                        CourseSelectInput.SELECT_REGIME -> RollSelector(
                            list = regimeList.toList(),
                            startOffset = state.editCourse.course.regime,
                        ) {
                            courseInternal = courseInternal.copy(regime = it)
                            selectedInput = null
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SaveDecline(
    modifier: Modifier = Modifier,
    onSave: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onDelete,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.error),
            modifier = Modifier
                .height(52.dp)
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.mycourse_delete),
                style = Typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
        Spacer(Modifier.width(16.dp))
        Button(
            onClick = onSave,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .height(52.dp)
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.settings_save),
                style = Typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
