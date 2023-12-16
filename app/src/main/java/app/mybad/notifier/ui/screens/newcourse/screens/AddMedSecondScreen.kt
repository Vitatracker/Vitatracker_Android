package app.mybad.notifier.ui.screens.newcourse.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ParameterIndicatorSelector
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.common.VerticalSpacerMedium
import app.mybad.notifier.ui.common.showToast
import app.mybad.notifier.ui.screens.newcourse.CreateCourseContract
import app.mybad.notifier.ui.screens.newcourse.common.BasicKeyboardInput
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.theme.MyBADTheme
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
    val context = LocalContext.current

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        sendEvent(CreateCourseContract.Event.ConfirmBack(false))
        effectFlow?.collect { effect ->
            when (effect) {
                is CreateCourseContract.Effect.Navigation -> navigation(effect)
                is CreateCourseContract.Effect.Collapse -> {}
                is CreateCourseContract.Effect.Expand -> {}
                is CreateCourseContract.Effect.Toast -> context.showToast(effect.message)
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
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 16.dp),
        ) {
            SecondScreenContent(
                state = state,
                sendEvent = sendEvent
            )
            ReUseFilledButton(textId = R.string.navigation_next) {
                sendEvent(CreateCourseContract.Event.ActionNext)
            }
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

    Column {
        Text(
            text = stringResource(R.string.add_med_reception),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        VerticalSpacerMedium()
        // при редактировании изменить и в MyCourseEditScreen, дублируется код с разным дизайном
        MultiBox(
            {
                // форма выпуска
                ParameterIndicatorSelector(
                    parameter = R.string.add_med_form,
                    value = state.remedy.type,
                    items = types,
                    onSelect = { sendEvent(CreateCourseContract.Event.UpdateRemedyType(it)) },
                )
            },
            {
                // дозировка
                BasicKeyboardInput(
                    label = R.string.add_med_dose,
                    init = if (state.remedy.dose == 0) "" else state.remedy.dose.toString(),
                    hideOnGo = true,
                    keyboardType = KeyboardType.Number,
                    alignRight = true,
                    prefix = {
                        Text(
                            text = stringResource(id = R.string.add_med_dose),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
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
                // измерение
                ParameterIndicatorSelector(
                    parameter = R.string.add_med_unit,
                    value = state.remedy.measureUnit,
                    items = units,
                    onSelect = { sendEvent(CreateCourseContract.Event.UpdateRemedyUnit(it)) },
                )
            },
            {
                // Правила приема лекарственных препаратов по отношению к еде: до еды, после еды, во время еды
                ParameterIndicatorSelector(
                    parameter = R.string.add_med_food_relation,
                    value = state.remedy.beforeFood,
                    items = relations,
                    onSelect = { sendEvent(CreateCourseContract.Event.UpdateRemedyRelations(it)) },
                )
            },
            itemsPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
        )
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
