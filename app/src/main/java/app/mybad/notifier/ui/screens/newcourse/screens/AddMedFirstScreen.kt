package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseOutlinedTextField
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.screens.newcourse.CreateCourseContract
import app.mybad.notifier.ui.screens.newcourse.common.ColorSelector
import app.mybad.notifier.ui.screens.newcourse.common.IconSelector
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun AddMedFirstScreen(
    state: CreateCourseContract.State,
    effectFlow: Flow<CreateCourseContract.Effect>? = null,
    sendEvent: (event: CreateCourseContract.Event) -> Unit = {},
    navigation: (navigationEffect: CreateCourseContract.Effect.Navigation) -> Unit = {},
) {

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
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                ReUseOutlinedTextField(
                    value = state.remedy.name ?: "",
                    label = stringResource(R.string.enter_med_name),
                    onValueChanged = {
                        sendEvent(
                            CreateCourseContract.Event.UpdateRemedy(
                                state.remedy.copy(name = it)
                            )
                        )
                    },
                    isError = state.isError,
                    errorTextId = if (state.isError) R.string.add_med_error_empty_name else null,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done,
                    ),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.add_med_icon),
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                MultiBox(
                    {
                        IconSelector(
                            selected = state.remedy.icon,
                            color = state.remedy.color,
                            onSelect = {
                                sendEvent(
                                    CreateCourseContract.Event.UpdateRemedy(
                                        state.remedy.copy(icon = it)
                                    )
                                )
                            }
                        )
                    },
                    itemsPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 20.dp,
                        bottom = 20.dp
                    ),
                    outlineColor = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.add_med_icon_color),
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                MultiBox(
                    {
                        ColorSelector(
                            selected = state.remedy.color,
                            onSelect = {
                                sendEvent(
                                    CreateCourseContract.Event.UpdateRemedy(
                                        state.remedy.copy(color = it)
                                    )
                                )
                            }
                        )
                    },
                    itemsPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 20.dp,
                        bottom = 20.dp
                    ),
                    outlineColor = MaterialTheme.colorScheme.primary,
                )
            }
            ReUseFilledButton(
                textId = R.string.navigation_next
            ) {
                sendEvent(CreateCourseContract.Event.ActionNext)
            }
        }
    }
}

@Preview
@Composable
fun AddMedFirstScreenPreview() {
    MyBADTheme {
        AddMedFirstScreen(
            state = CreateCourseContract.State(),
        )
    }
}
