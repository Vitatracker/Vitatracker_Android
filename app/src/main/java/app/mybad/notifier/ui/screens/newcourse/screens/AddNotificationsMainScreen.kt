package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.ui.screens.newcourse.NewCourseIntent
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.newcourse.common.TimeSelector
import app.mybad.notifier.ui.theme.Typography
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
@Preview
fun AddNotificationsMainScreen(
    modifier: Modifier = Modifier,
    reducer: (NewCourseIntent) -> Unit = {},
    med: MedDomainModel = MedDomainModel(),
    onNext: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    val forms = stringArrayResource(R.array.types)
    var notificationsPattern by remember { mutableStateOf(emptyList<Pair<LocalTime, Int>>()) }
    var dialogIsShown by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<Pair<LocalTime, Int>?>(null) }

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
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                onClick = {
                    notificationsPattern = notificationsPattern.toMutableList().apply {
                        add(Pair(LocalTime.now().withSecond(0), 1))
                    }
                }
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
                    text = stringResource(R.string.add_notifications_time),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(Modifier.height(16.dp))
            LazyColumn {
                notificationsPattern.forEachIndexed { index, item ->
                    item {
                        NotificationItem(
                            time = item.first,
                            quantity = item.second,
                            form = med.type,
                            forms = forms,
                            onDelete = {
                                notificationsPattern = notificationsPattern.toMutableList().apply {
                                    removeAt(index)
                                }.toList()
                            },
                            onDoseChange = { q ->
                                notificationsPattern = notificationsPattern.toMutableList()
                                    .apply {
                                        removeAt(index)
                                        add(index, item.copy(second = q.toInt()))
                                    }.toList()
                            },
                            onTimeClick = {
                                selectedItem = item
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
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
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

    if (dialogIsShown && selectedItem != null) {
        Dialog(onDismissRequest = { dialogIsShown = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                TimeSelector(
                    initTime = selectedItem!!.first,
                    onSelect = {
                        val n = notificationsPattern.toMutableList()
                        val i = n.indexOf(selectedItem!!)
                        n.removeAt(i)
                        selectedItem = selectedItem!!.copy(first = it)
                        n.add(i, selectedItem!!)
                        n.sortBy { item -> item.first }
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
    time: LocalTime,
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
                        interactionSource = MutableInteractionSource(),
                        onClick = onDelete::invoke
                    )
            )
            Text(
                text = time.format(DateTimeFormatter.ofPattern("HH:mm")),
                style = Typography.bodyLarge,
                modifier = Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
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
                    modifier = Modifier.width(25.dp).onFocusChanged {
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
