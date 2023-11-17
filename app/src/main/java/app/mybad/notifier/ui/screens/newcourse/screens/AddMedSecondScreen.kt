package app.mybad.notifier.ui.screens.newcourse.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ParameterIndicator
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.screens.newcourse.CreateCourseContract
import app.mybad.notifier.ui.screens.newcourse.common.BasicKeyboardInput
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun AddMedSecondScreen(
    state: CreateCourseContract.State,
    effectFlow: Flow<CreateCourseContract.Effect>? = null,
    sendEvent: (event: CreateCourseContract.Event) -> Unit = {},
    navigation: (navigationEffect: CreateCourseContract.Effect.Navigation) -> Unit = {},
) {
    Log.w("VTTAG", "NewCourseNavGraph::AddMedSecondScreen: start")
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is CreateCourseContract.Effect.Navigation -> navigation(effect)
                is CreateCourseContract.Effect.Collapse -> {}
                is CreateCourseContract.Effect.Expand -> {}
            }
        }
    }

    Scaffold(topBar = {
        ReUseTopAppBar(
            titleResId = R.string.add_med_title,
            onBackPressed = { sendEvent(CreateCourseContract.Event.ActionBack) }
        )
    }) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = 16.dp
                ),
        ) {
            SecondScreenContent(
                state = state,
                sendEvent = sendEvent
            )
        }
    }
}

@Composable
private fun SecondScreenContent(
    state: CreateCourseContract.State,
    sendEvent: (event: CreateCourseContract.Event) -> Unit,
) {
    val types = stringArrayResource(R.array.types)
    val units = stringArrayResource(R.array.units)
    val relations = stringArrayResource(R.array.food_relations)
    val dose = stringResource(R.string.add_med_dose)
    val unit = stringResource(R.string.add_med_unit)

    Column {
        Text(
            text = stringResource(R.string.add_med_reception),
            style = Typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        MultiBox(
            {
                var menuExpanded by remember { mutableStateOf(false) }
                ParameterIndicator(
                    name = stringResource(R.string.add_med_form),
                    value = types[state.remedy.type],
                    onClick = { menuExpanded = true }
                )
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    offset = DpOffset(x = 300.dp, y = 0.dp)
                ) {
                    types.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                sendEvent(
                                    CreateCourseContract.Event.UpdateRemedy(
                                        state.remedy.copy(type = index)
                                    )
                                )
                                menuExpanded = false
                            }
                        )
                    }
                }
            },
            {
                BasicKeyboardInput(
                    label = dose,
                    init = if (state.remedy.dose == 0) "" else state.remedy.dose.toString(),
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
                        sendEvent(
                            CreateCourseContract.Event.UpdateRemedy(
                                state.remedy.copy(dose = it.toIntOrNull() ?: 0)
                            )
                        )
                    }
                )
            },
            {
                var menuExpanded by remember { mutableStateOf(false) }
                ParameterIndicator(
                    name = unit,
                    value = units[state.remedy.measureUnit],
                    onClick = { menuExpanded = true }
                )
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    offset = DpOffset(x = 300.dp, y = 0.dp)
                ) {
                    units.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                sendEvent(
                                    CreateCourseContract.Event.UpdateRemedy(
                                        state.remedy.copy(measureUnit = index)
                                    )
                                )
                                menuExpanded = false
                            }
                        )
                    }
                }
            },
            {
                var menuExpanded by remember { mutableStateOf(false) }
                ParameterIndicator(
                    name = stringResource(R.string.add_med_food_relation),
                    value = relations[state.remedy.beforeFood],
                    onClick = { menuExpanded = true }
                )
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    offset = DpOffset(x = 300.dp, y = 0.dp)
                ) {
                    relations.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                sendEvent(
                                    CreateCourseContract.Event.UpdateRemedy(
                                        state.remedy.copy(beforeFood = index)
                                    )
                                )
                                menuExpanded = false
                            }
                        )
                    }
                }
            },
            itemsPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 20.dp),
            outlineColor = MaterialTheme.colorScheme.primary,
        )
    }
    ReUseFilledButton(
        textId = R.string.navigation_next
    ) {
        sendEvent(CreateCourseContract.Event.ActionNext)
    }
}

@Preview
@Composable
fun AddMedSecondScreenPreview() {
    MyBADTheme {
        AddMedSecondScreen(
            state = CreateCourseContract.State(),
        )
    }
}
