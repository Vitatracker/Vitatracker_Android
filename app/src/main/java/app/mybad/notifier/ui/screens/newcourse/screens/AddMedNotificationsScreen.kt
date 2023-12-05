package app.mybad.notifier.ui.screens.newcourse.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.data.models.UsageFormat
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.AddNotificationButton
import app.mybad.notifier.ui.common.NotificationItem
import app.mybad.notifier.ui.common.ReUseFilledButton
import app.mybad.notifier.ui.common.ReUseProgressDialog
import app.mybad.notifier.ui.common.ReUseTopAppBar
import app.mybad.notifier.ui.common.showToast
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
    var canPress by remember { mutableStateOf(true) }
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
                .padding(paddingValues)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 0.dp,
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(0.5f)
            ) {
                Text(
                    text = stringResource(id = R.string.add_notifications_time_set),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(12.dp))
                AddNotificationButton(
                    form = state.remedy.type,
                    forms = forms,
                    onClick = { sendEvent(CreateCourseContract.Event.AddUsagesPattern) }
                )
                Spacer(Modifier.height(16.dp))
                LazyColumn(
                    state = rememberLazyListState(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 72.dp)
                ) {
                    items(
                        items = state.usagesPattern,
                        key = { pattern -> pattern.timeInMinutes }
                    ) { pattern ->
                        Log.w(
                            "VTTAG",
                            "AddMedNotificationsScreen::items: pattern=${pattern.timeInMinutes} - ${pattern.quantity}"
                        )
                        NotificationItem(
                            time = pattern.timeInMinutes,
                            quantity = pattern.quantity,
                            form = state.remedy.type,
                            forms = forms,
                            onDelete = {
                                sendEvent(CreateCourseContract.Event.DeleteUsagePattern(pattern))
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
                textId = R.string.navigation_next,
                enabled = state.nextAllowed && !state.loader,
            ) {
                if (canPress) {
                    // запретим нажимать кнопку
//                    canPress = false
                    sendEvent(CreateCourseContract.Event.Finish)
                }
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
    AnimatedVisibility(visible = state.loader) {
        ReUseProgressDialog()
        // разрешим нажимать кнопку
//        canPress = true
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
