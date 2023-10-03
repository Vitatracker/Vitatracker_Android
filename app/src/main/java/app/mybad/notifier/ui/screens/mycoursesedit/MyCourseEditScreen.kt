package app.mybad.notifier.ui.screens.mycoursesedit

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
import app.mybad.data.models.CourseSelectInput
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.CalendarAndRegimeSelectorDialog
import app.mybad.notifier.ui.common.ParameterIndicator
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.common.usagesPatternPreview
import app.mybad.notifier.ui.screens.newcourse.common.BasicKeyboardInput
import app.mybad.notifier.ui.screens.newcourse.common.ColorSelector
import app.mybad.notifier.ui.screens.newcourse.common.IconSelector
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import app.mybad.utils.displayDateFull
import kotlinx.coroutines.flow.Flow

@Composable
fun MyCourseEditScreen(
    state: MyCoursesEditContract.State,
    effectFlow: Flow<MyCoursesEditContract.Effect>? = null,
    sendEvent: (event: MyCoursesEditContract.Event) -> Unit = {},
    navigation: (navigationEffect: MyCoursesEditContract.Effect.Navigation) -> Unit = {},
) {
    val types = stringArrayResource(R.array.types)
    val units = stringArrayResource(R.array.units)
    val relations = stringArrayResource(R.array.food_relations)

    val name = stringResource(R.string.add_med_name)
    val form = stringResource(R.string.add_med_form)
    val dose = stringResource(R.string.mycourse_dosage_and_usage)
    val unit = stringResource(R.string.add_med_unit)
    val rel = stringResource(R.string.add_med_food_relation)

    val startLabel = stringResource(R.string.add_course_start_time)
    val endLabel = stringResource(R.string.add_course_end_time)
    val regimeLabel = stringResource(R.string.medication_regime)
    val regimeList = stringArrayResource(R.array.regime)

    var remedyInternal by remember(state.course.id) { mutableStateOf(state.remedy) }
    var courseInternal by remember(state.course.id) { mutableStateOf(state.course) }

    var selectedInput: CourseSelectInput? by remember { mutableStateOf(null) }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is MyCoursesEditContract.Effect.Navigation -> navigation(effect)
            }
        }
    }

    Log.w(
        "VTTAG",
        "MyCourseEditScreen::Init: id=${remedyInternal.id} remedy=${remedyInternal.name} courseId=${
            courseInternal.id
        } usagesPattern=${state.usagesPatternEdit.size}"
    )

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
                            value = types[remedyInternal.type],
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
                            value = relations[remedyInternal.beforeFood],
                            onClick = { exp = true }
                        )
                        DropdownMenu(
                            expanded = exp,
                            onDismissRequest = { exp = false },
                            offset = DpOffset(x = 300.dp, y = 0.dp)
                        ) {
                            relations.forEachIndexed { index, item ->
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
                            value = courseInternal.startDate.displayDateFull(),
                            onClick = { selectedInput = CourseSelectInput.SELECT_START_DATE }
                        )
                    },
                    {
                        ParameterIndicator(
                            name = endLabel,
                            value = courseInternal.endDate.displayDateFull(),
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
                        Text(text = stringResource(R.string.add_notifications_time_set))
                    },
                    itemsPadding = PaddingValues(16.dp),
                    outlineColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            sendEvent(MyCoursesEditContract.Event.NotificationEditing)
                        }
                    )
                )
            }
            Spacer(Modifier.height(16.dp))
            SaveDecline(
                onSave = {
                    sendEvent(
                        MyCoursesEditContract.Event.UpdateAndEnd(
                            courseInternal,
                            remedyInternal,
                        )
                    )
                },
                onDelete = {
                    sendEvent(MyCoursesEditContract.Event.Delete(courseInternal.id))
                },
            )
        }

        selectedInput?.let { select ->
            CalendarAndRegimeSelectorDialog(
                selectedInput = select,
                startDate = courseInternal.startDate,
                endDate = courseInternal.endDate,
                regime = courseInternal.regime,
                regimeList = regimeList.toList(),
                onDismissRequest = { selectedInput = null },
                onDateSelected = {
                    courseInternal = courseInternal.copy(
                        startDate = it.first,
                        endDate = it.second,
                    )
                },
                onRegimeSelected = {
                    courseInternal = courseInternal.copy(regime = it)
                }
            )
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

@Preview
@Composable
fun MyCourseEditScreenPreview() {
    MyBADTheme {
        MyCourseEditScreen(
            MyCoursesEditContract.State(
                usagesPatternEdit = usagesPatternPreview,
            )
        )
    }
}
