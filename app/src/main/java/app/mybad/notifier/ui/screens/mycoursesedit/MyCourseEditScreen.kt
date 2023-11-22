package app.mybad.notifier.ui.screens.mycoursesedit

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import app.mybad.notifier.ui.common.ReUseAlertDialog
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.common.ReUseTwoButtons
import app.mybad.notifier.ui.common.usagesPatternPreview
import app.mybad.notifier.ui.screens.newcourse.common.BasicKeyboardInput
import app.mybad.notifier.ui.screens.newcourse.common.ColorSelector
import app.mybad.notifier.ui.screens.newcourse.common.IconSelector
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.theme.MyBADTheme
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

    var selectedInput: CourseSelectInput? by remember(state.course.id) { mutableStateOf(null) }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is MyCoursesEditContract.Effect.Navigation -> navigation(effect)
            }
        }
    }

    Log.d(
        "VTTAG",
        "MyCourseEditScreen::Init: remedy id=${remedyInternal.id} name=${remedyInternal.name} courseId=${
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
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = stringResource(R.string.mycourse_edit_icon),
                    style = MaterialTheme.typography.bodyLarge,
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
                    style = MaterialTheme.typography.bodyLarge,
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
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                MultiBox(
                    {
                        BasicKeyboardInput(
                            label = name,
                            init = remedyInternal.name,
                            style = MaterialTheme.typography.bodyLarge,
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
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
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
                )
                Text(
                    text = stringResource(R.string.mycourse_duration),
                    style = MaterialTheme.typography.bodyLarge,
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
                Spacer(Modifier.height(16.dp))
                MultiBox(
                    {
                        Text(text = stringResource(R.string.add_notifications_time_set))
                    },
                    itemsPadding = PaddingValues(16.dp),
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            sendEvent(
                                MyCoursesEditContract.Event.Update(
                                    courseInternal,
                                    remedyInternal,
                                )
                            )
                            sendEvent(MyCoursesEditContract.Event.NotificationEditing)
                        }
                    )
                )
            }
            Spacer(Modifier.height(16.dp))
            ReUseTwoButtons(
                modifier = Modifier.fillMaxWidth(),
                confirmId = R.string.settings_save,
                errorColor = true,
                onConfirm = {
                    sendEvent(
                        MyCoursesEditContract.Event.Update(
                            courseInternal,
                            remedyInternal,
                        )
                    )
                    sendEvent(MyCoursesEditContract.Event.Save)
                },
                dismissId = R.string.mycourse_delete,
                onDismiss = {
                    sendEvent(MyCoursesEditContract.Event.ConfirmationDelete)
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

        if (state.confirmation) {
            ReUseAlertDialog(
                drawableId = R.drawable.medicines,
                textId = R.string.mycourse_confirmation_delete_text,
                dismissId = R.string.mycourse_delete,
                confirmId = R.string.mycourse_delete_cancel,
                textButton = false,
                dismissErrorColor = true,
                onDismiss = { sendEvent(MyCoursesEditContract.Event.Delete(courseInternal.id)) },
                onConfirm = { sendEvent(MyCoursesEditContract.Event.CancelDelete) },
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
