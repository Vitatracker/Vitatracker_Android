package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.data.models.UsageFormat
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.AddNotificationButton
import app.mybad.notifier.ui.common.NotificationItem
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.common.usagesPatternPreview
import app.mybad.notifier.ui.screens.newcourse.CreateCourseContract
import app.mybad.notifier.ui.screens.newcourse.common.TimeSelectorDialog
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
fun AddMedNotificationsScreen(
    state: CreateCourseContract.State,
    effectFlow: Flow<CreateCourseContract.Effect>? = null,
    sendEvent: (event: CreateCourseContract.Event) -> Unit = {},
    navigation: (navigationEffect: CreateCourseContract.Effect.Navigation) -> Unit = {},
) {
    val forms = stringArrayResource(R.array.types)
    var selectedItem: UsageFormat? by remember { mutableStateOf(null) }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is CreateCourseContract.Effect.Navigation -> navigation(effect)
                is CreateCourseContract.Effect.Collapse -> {}
                is CreateCourseContract.Effect.Expand -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            ReUseTopAppBar(
                titleResId = R.string.add_notifications_choose_time,
                onBackPressed = { sendEvent(CreateCourseContract.Event.ActionBack) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = paddingValues.calculateTopPadding(),
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = R.string.add_notifications_time_set),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(12.dp))
                AddNotificationButton(
                    form = state.remedy.type,
                    forms = forms,
                    onClick = { sendEvent(CreateCourseContract.Event.AddUsagesPattern) }
                )
                Spacer(Modifier.height(16.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 72.dp)
                ) {
                    items(
                        items = state.usagesPattern,
                        key = { pattern -> pattern.timeInMinutes }
                    ) { pattern ->
                        NotificationItem(
                            time = pattern.timeInMinutes,
                            quantity = pattern.quantity,
                            form = state.remedy.type,
                            forms = forms,
                            onDelete = {
                                sendEvent(
                                    CreateCourseContract.Event.DeleteUsagePattern(
                                        pattern
                                    )
                                )
                            },
                            onDoseChange = {
                                sendEvent(
                                    CreateCourseContract.Event.ChangeQuantityUsagePattern(
                                        pattern = pattern,
                                        quantity = it
                                    )
                                )
                            },
                            onTimeClick = { selectedItem = pattern }
                        )
                    }
                }
            }
            ReUseFilledButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textId = R.string.navigation_next,
                isEnabled = state.nextAllowed,
            ) {
                sendEvent(CreateCourseContract.Event.Finish)
                sendEvent(CreateCourseContract.Event.ActionNext)
            }
        }
    }
    selectedItem?.let {
        TimeSelectorDialog(
            initTime = it.timeInMinutes,
            onDismiss = { selectedItem = null },
        ) { time ->
            sendEvent(
                CreateCourseContract.Event.ChangeTimeUsagePattern(
                    pattern = it,
                    time = time
                )
            )
            selectedItem = null
        }
    }
}

@Preview
@Composable
fun AddMedNotificationsScreenPreview() {
    MyBADTheme {
        AddMedNotificationsScreen(
            state = CreateCourseContract.State(
                usagesPattern = usagesPatternPreview,
                nextAllowed = true,
            ),
        )
    }
}
