package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.screens.newcourse.CreateCourseScreensContract
import app.mybad.notifier.ui.screens.newcourse.common.ColorSelector
import app.mybad.notifier.ui.screens.newcourse.common.IconSelector
import app.mybad.notifier.ui.screens.newcourse.common.MultiBox
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.notifier.ui.screens.reuse.ReUseOutlinedTextField
import app.mybad.notifier.ui.screens.reuse.TopAppBarWithBackAction
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun AddMedicineFirstScreen(
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
            onBackPressed = { onEventSent(CreateCourseScreensContract.Event.ActionBack) }
        )
    }) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.add_med_name),
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                ReUseOutlinedTextField(
                    value = state.med.name ?: "",
                    label =stringResource(R.string.enter_med_name),
                    onValueChanged = {
                        onEventSent(CreateCourseScreensContract.Event.UpdateMedName(it))
                    },
                    isError = state.isError,
                    errorTextId = if (state.isError) R.string.add_med_error_empty_name else null
                )
//                MultiBox(
//                    {
//                        Row(
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            modifier = Modifier.fillMaxWidth(),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            BasicKeyboardInput(
//                                modifier = Modifier.width(300.dp),
//                                label = stringResource(R.string.enter_med_name),
//                                init = state.med.name,
//                                hideOnGo = true,
//                                onChange = {
//                                    onEventSent(CreateCourseScreensContract.Event.UpdateMed(state.med.copy(name = it)))
//                                    isError = it.isBlank()
//                                }
//                            )
//                            if (isError) {
//                                Icon(
//                                    imageVector = Icons.Rounded.Error,
//                                    contentDescription = null,
//                                    tint = MaterialTheme.colorScheme.error
//                                )
//                            }
//                        }
//
//                    },
//                    itemsPadding = PaddingValues(16.dp),
//                    outlineColor = if (isError) MaterialTheme.colorScheme.errorContainer
//                    else MaterialTheme.colorScheme.primary,
//                )
//                if (isError) {
//                    Text(
//                        modifier = Modifier.padding(top = 4.dp),
//                        style = Typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.error,
//                        text = stringResource(id = R.string.add_med_error_empty_name)
//                    )
//                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.add_med_icon),
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                MultiBox(
                    {
                        IconSelector(
                            selected = state.med.icon,
                            color = state.med.color,
                            onSelect = {
                                onEventSent(CreateCourseScreensContract.Event.UpdateMed(state.med.copy(icon = it)))
                            }
                        )
                    },
                    itemsPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
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
                            selected = state.med.color,
                            onSelect = {
                                onEventSent(CreateCourseScreensContract.Event.UpdateMed(state.med.copy(color = it)))
                            }
                        )
                    },
                    itemsPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                    outlineColor = MaterialTheme.colorScheme.primary,
                )
            }
            ReUseFilledButton(
                modifier = Modifier.fillMaxWidth(),
                textId = R.string.navigation_next
            ) {
                onEventSent(CreateCourseScreensContract.Event.ActionNext)
            }
        }
    }
}
