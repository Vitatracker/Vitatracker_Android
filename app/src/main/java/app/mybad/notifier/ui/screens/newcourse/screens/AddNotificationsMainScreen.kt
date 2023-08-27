package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import app.mybad.notifier.ui.screens.newcourse.CreateCourseScreensContract
import app.mybad.notifier.ui.screens.newcourse.common.BaseSelector
import app.mybad.notifier.ui.screens.newcourse.common.TimeSelector
import app.mybad.notifier.ui.screens.reuse.ReUseFilledButton
import app.mybad.notifier.ui.screens.reuse.TopAppBarWithBackAction
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.ui.theme.primaryBorderGray
import app.mybad.notifier.utils.getFormsPluralsArray
import app.mybad.notifier.utils.toTimeDisplay
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@Composable
@Preview
fun AddNotificationsMainScreen(
    state: CreateCourseScreensContract.State = CreateCourseScreensContract.State(),
    events: Flow<CreateCourseScreensContract.Effect>? = null,
    onEventSent: (event: CreateCourseScreensContract.Event) -> Unit = {},
    onNavigationRequested: (navigationEffect: CreateCourseScreensContract.Effect.Navigation) -> Unit = {}
) {
    val pluralsArray = getFormsPluralsArray()
    var selectedItem by remember { mutableStateOf<Triple<Int, Long, Int>?>(null) }
    var showDialog by remember {
        mutableIntStateOf(-1)
    }

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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBarWithBackAction(
                titleResId = R.string.add_notifications_choose_time,
                onBackPressed = { onEventSent(CreateCourseScreensContract.Event.ActionBack) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
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
                    onClick = {
                        onEventSent(CreateCourseScreensContract.Event.AddUsagesPattern)
                    }
                )
                Spacer(Modifier.height(16.dp))
                LazyColumn(contentPadding = PaddingValues(bottom = 72.dp)) {
                    itemsIndexed(state.usagesPattern) { index, item ->
                        NotificationItem(
                            time = item.first,
                            quantity = item.second,
                            form = state.med.type,
                            forms = pluralsArray,
                            onDelete = {
                                onEventSent(CreateCourseScreensContract.Event.RemoveUsagesPattern(index))
                            },
                            onDoseChange = { q ->
                                val newPattern = item.copy(second = q.toInt())
                                onEventSent(CreateCourseScreensContract.Event.UpdateUsagePattern(index, newPattern))
                            },
                            onTimeClick = {
                                selectedItem = Triple(index, item.first, item.second)
                                showDialog = 1
                            },
                            onDoseClicked = {
                                selectedItem = Triple(index, item.first, item.second)
                                showDialog = 2
                            }
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
            ReUseFilledButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                textId = R.string.navigation_next,
                isEnabled = state.nextAllowed
            ) {
                onEventSent(CreateCourseScreensContract.Event.Finish)
                onEventSent(CreateCourseScreensContract.Event.ActionNext)
            }
        }

        if (showDialog != -1) {
            when (showDialog) {
                1 -> {
                    selectedItem?.let {
                        TimeSelectionDialog(
                            selectedItem = it,
                            onDismissRequest = {
                                showDialog = -1
                            },
                            onEventSent = onEventSent
                        )
                    }
                }

                2 -> {
                    selectedItem?.let {
                        EnterDoseDialog(
                            selectedItem = it,
                            onDismissRequest = {
                                showDialog = -1
                            },
                            onEventSent = onEventSent
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun NotificationItem(
    modifier: Modifier = Modifier,
    time: Long = 0L,
    quantity: Int = 1,
    form: Int = 0,
    forms: Array<Int> = arrayOf(R.plurals.plurals_types_tablet),
    onDelete: () -> Unit = {},
    onTimeClick: () -> Unit = {},
    onDoseChange: (Float) -> Unit = { },
    onDoseClicked: () -> Unit = { }
) {
    var field by remember { mutableStateOf(TextFieldValue(quantity.toString())) }

    LaunchedEffect(quantity) {
        val q = quantity.toString()
        field = field.copy(
            text = q,
            selection = if (quantity == 0) TextRange(0, 1) else TextRange(q.length, q.length),
        )
    }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(1.dp, primaryBorderGray),
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.RemoveCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .size(24.dp)
                    .clickable(
                        onClick = onDelete
                    )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = time.toTimeDisplay(),
                    style = Typography.bodyLarge,
                    modifier = Modifier
                        .clickable(
                            onClick = onTimeClick
                        )
                )
                val textPlurals = LocalContext.current.resources.getQuantityString(
                    forms[form],
                    quantity,
                    quantity
                )
                Text(
                    modifier = Modifier
                        .clickable(onClick = onDoseClicked),
                    text = textPlurals,
                    style = Typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Preview
@Composable
private fun AddNotificationButton(
    onClick: () -> Unit = {},
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(1.dp, primaryBorderGray),
        modifier = Modifier
            .clickable(
                onClick = onClick
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(R.string.add_notifications_choose_time),
                style = Typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun TimeSelectionDialog(
    selectedItem: Triple<Int, Long, Int>,
    onDismissRequest: () -> Unit = {},
    onEventSent: (event: CreateCourseScreensContract.Event) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            TimeSelector(
                initTime = selectedItem.second,
                onSelect = {
                    val newPattern = Pair(first = it, second = selectedItem.third)
                    onEventSent(CreateCourseScreensContract.Event.UpdateUsagePattern(selectedItem.first, newPattern))
                    onDismissRequest()
                }
            )
        }
    }
}

@Composable
private fun EnterDoseDialog(
    selectedItem: Triple<Int, Long, Int>,
    onDismissRequest: () -> Unit = {},
    onEventSent: (event: CreateCourseScreensContract.Event) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            BaseSelector(
                initValue = selectedItem.third,
                values = (0..1000).toList(),
                headerResId = null,
                onSelect = {
                    val newPattern = Pair(first = selectedItem.second, second = it)
                    onEventSent(CreateCourseScreensContract.Event.UpdateUsagePattern(selectedItem.first, newPattern))
                    onDismissRequest()
                }
            )
        }
    }
}
