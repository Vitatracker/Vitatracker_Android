package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.screens.common.ParameterIndicator
import app.mybad.notifier.ui.screens.newcourse.CreateCourseScreensContract
import app.mybad.notifier.ui.screens.newcourse.common.BasicKeyboardInput
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.notifier.ui.screens.reuse.TopAppBarWithBackAction
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun AddMedicineSecondScreen(
    state: CreateCourseScreensContract.State,
    events: Flow<CreateCourseScreensContract.Effect>? = null,
    onEventSent: (event: CreateCourseScreensContract.Event) -> Unit = {},
    onNavigationRequested: (navigationEffect: CreateCourseScreensContract.Effect.Navigation) -> Unit
) {
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

    Scaffold(topBar = {
        TopAppBarWithBackAction(
            titleResId = R.string.add_med_h,
            onBackPressed = {
                onEventSent(CreateCourseScreensContract.Event.ActionBack)
            }
        )
    }) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            SecondScreenContent(
                state = state,
                onEventSent = onEventSent
            )
        }
    }
}

@Composable
private fun SecondScreenContent(
    state: CreateCourseScreensContract.State,
    onEventSent: (event: CreateCourseScreensContract.Event) -> Unit
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
                var exp by remember { mutableStateOf(false) }
                ParameterIndicator(
                    name = stringResource(R.string.add_med_form),
                    value = types[state.med.type],
                    onClick = {
                        exp = true
                    }
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
                                onEventSent(CreateCourseScreensContract.Event.UpdateMed(state.med.copy(type = index)))
                                exp = false
                            }
                        )
                    }
                }
            },
            {
                BasicKeyboardInput(
                    label = dose,
                    init = if (state.med.dose == 0) "" else state.med.dose.toString(),
                    hideOnGo = true,
                    keyboardType = KeyboardType.Number,
                    alignRight = true,
                    prefix = {
                        Text(text = dose, style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                    },
                    onChange = {
                        onEventSent(CreateCourseScreensContract.Event.UpdateMed(state.med.copy(dose = it.toIntOrNull() ?: 0)))
                    }
                )
            },
            {
                var exp by remember { mutableStateOf(false) }
                ParameterIndicator(name = unit, value = units[state.med.measureUnit], onClick = {
                    exp = true
                })
                DropdownMenu(
                    expanded = exp,
                    onDismissRequest = { exp = false },
                    offset = DpOffset(x = 300.dp, y = 0.dp)
                ) {
                    units.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                onEventSent(CreateCourseScreensContract.Event.UpdateMed(state.med.copy(measureUnit = index)))
                                exp = false
                            }
                        )
                    }
                }
            },
            {
                var exp by remember { mutableStateOf(false) }
                ParameterIndicator(
                    name = stringResource(R.string.add_med_food_relation),
                    value = relations[state.med.beforeFood],
                    onClick = {
                        exp = true
                    }
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
                                onEventSent(CreateCourseScreensContract.Event.UpdateMed(state.med.copy(beforeFood = index)))
                                exp = false
                            }
                        )
                    }
                }
            },
            modifier = Modifier,
            itemsPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 20.dp),
        )
    }
    ReUseFilledButton(
        modifier = Modifier.fillMaxWidth(),
        textId = R.string.navigation_next
    ) {
        onEventSent(CreateCourseScreensContract.Event.ActionNext)
    }
}
