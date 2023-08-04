package app.mybad.notifier.ui.screens.newcourse.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.mybad.data.models.UsageFormat
import app.mybad.domain.models.RemedyDomainModel
import app.mybad.notifier.ui.screens.newcourse.NewCourseIntent
import app.mybad.notifier.ui.screens.newcourse.common.TimeSelector
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R
import app.mybad.theme.utils.currentTimeInMinutes
import app.mybad.theme.utils.minutesToHHmm
import app.mybad.theme.utils.toTimeDisplay

@Composable
@Preview
fun AddNotificationsMainScreen(
    modifier: Modifier = Modifier,
    reducer: (NewCourseIntent) -> Unit = {},
    remedy: RemedyDomainModel = RemedyDomainModel(),
    onNext: () -> Unit = {}
) {
    val forms = stringArrayResource(R.array.types)
    var notificationsPattern by remember { mutableStateOf<List<UsageFormat>>(listOf()) }
    var dialogIsShown by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(UsageFormat(-1, 0)) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.add_notifications_time_set),
                modifier = Modifier.padding(bottom = 4.dp),
                style = MaterialTheme.typography.bodyLarge
            )
            AddNotificationButton(
                form = remedy.type,
                forms = forms,
                onClick = {
                    notificationsPattern = notificationsPattern.plus(
                        // тут UTC время
                        UsageFormat(timeInMinutes = currentTimeInMinutes(), quantity = 1)
                    )
                }
            )
            Spacer(Modifier.height(16.dp))
            LazyColumn {
                notificationsPattern.forEachIndexed { index, usage ->
                    item {
                        NotificationItem(
                            time = usage.timeInMinutes,// тут время в UTC
                            quantity = usage.quantity,
                            form = remedy.type,
                            forms = forms,
                            onDelete = {
                                notificationsPattern =
                                    notificationsPattern.minus(notificationsPattern[index])
                            },
                            onDoseChange = { q ->
                                notificationsPattern = notificationsPattern.toMutableList()
                                    .apply {
                                        removeAt(index)
                                        add(index, usage.copy(quantity = q.toInt()))
                                    }.toList()
                            },
                            onTimeClick = {
                                selectedItem = usage
                                dialogIsShown = true
                            }
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                reducer(NewCourseIntent.UpdateUsagesPattern(notificationsPattern))
                onNext()
            }
        ) {
            Text(
                text = stringResource(R.string.navigation_next),
                style = Typography.bodyLarge
            )
        }
    }

    if (dialogIsShown && selectedItem.timeInMinutes >= 0) {
        Dialog(onDismissRequest = { dialogIsShown = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                TimeSelector(
                    initTime = selectedItem.timeInMinutes,
                    onSelect = {
                        val n = notificationsPattern.toMutableList()
                        val i = n.indexOf(selectedItem)
                        n.removeAt(i)
                        selectedItem = selectedItem.copy(timeInMinutes = it)
                        n.add(i, selectedItem)
                        n.sortBy { usageFormat -> usageFormat.timeInMinutes }
                        notificationsPattern = n.toList()
                        dialogIsShown = false
                    }
                )
            }
        }
    }
}

@Composable
private fun NotificationItem(
    modifier: Modifier = Modifier,
    time: Int, // время в UTC
    quantity: Int,
    form: Int,
    forms: Array<String>,
    onDelete: () -> Unit,
    onTimeClick: () -> Unit = {},
    onDoseChange: (Float) -> Unit = { }
) {
    val fm = LocalFocusManager.current
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
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.RemoveCircleOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(16.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onDelete::invoke
                    )
            )
            Log.w(
                "VTTAG",
                "TimeSelector::NotificationItem: time=${time.minutesToHHmm()} - ${time.toTimeDisplay()}"
            )
            Text(
                text = time.toTimeDisplay(),// отобразить время системное
                style = Typography.bodyLarge,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onTimeClick
                )
            )
            Row {
                BasicTextField(
                    value = field,
                    onValueChange = {
                        field = it
                        val res = it.text.toFloatOrNull() ?: 0f
                        onDoseChange(if (res > 10f) 10f else res)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            fm.clearFocus()
                            field = field.copy(selection = TextRange.Zero)
                        }
                    ),
                    modifier = Modifier
                        .width(25.dp)
                        .onFocusChanged {
                            if (it.hasFocus || it.isFocused) {
                                field = field.copy(selection = TextRange(0, field.text.length))
                            }
                        }
                )
                Text(
                    text = forms[form],
                    style = Typography.bodyMedium,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Spacer(Modifier.width(0.dp))
        }
    }
}

@Composable
private fun AddNotificationButton(
    modifier: Modifier = Modifier,
    form: Int,
    forms: Array<String>,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = onClick
        ),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AddCircleOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(16.dp)
            )
            Text(
                text = stringResource(R.string.add_notifications_choose_time),
                style = Typography.bodyLarge,
            )
            Row {
                Text(
                    text = "1 ${forms[form]}",
                    style = Typography.bodyMedium,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Spacer(Modifier.width(0.dp))
        }
    }
}
