package app.mybad.notifier.ui.screens.newcourse.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.ui.screens.newcourse.NewCourseIntent
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.common.NavigationRow
import app.mybad.notifier.ui.screens.newcourse.common.RollSelector
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
    var dialogIsForTime by remember { mutableStateOf(true) }
    var selectedItem by remember { mutableStateOf<Pair<LocalTime,Int>?>(null) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.add_notifications_time_set),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                onClick = {
                    notificationsPattern = notificationsPattern.toMutableList().apply {
                        add(Pair(LocalTime.MIDNIGHT.withHour(8),1))
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
                notificationsPattern.forEach {
                    item {
                        NotificationItem(
                            time = it.first,
                            quantity = it.second,
                            form = med.type,
                            forms = forms,
                            onDelete = { time ->
                                notificationsPattern = notificationsPattern.toMutableList()
                                    .apply{ removeIf { it.first == time} }.toList()
                            },
                            onDoseClick = {
                                selectedItem = it
                                dialogIsForTime = false
                                dialogIsShown = true
                            },
                            onTimeClick = {
                                selectedItem = it
                                dialogIsForTime = true
                                dialogIsShown = true
                            }
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
        NavigationRow(
            onBack = onBack::invoke,
            onNext = {
                reducer(NewCourseIntent.UpdateUsagesPattern(notificationsPattern))
                onNext()
            }
        )
    }

    if(dialogIsShown && selectedItem != null) {
        Dialog(onDismissRequest = { dialogIsShown = false }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                if(dialogIsForTime) {
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
                if(!dialogIsForTime) {
                    RollSelector(
                        list = (1..10).map { it.toString() }.toList(),
                        onSelect = {
                            val n = notificationsPattern.toMutableList()
                            val i = n.indexOf(selectedItem!!)
                            n.removeAt(i)
                            selectedItem = selectedItem!!.copy(second = it)
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
}

@Composable
private fun NotificationItem(
    modifier: Modifier = Modifier,
    time: LocalTime,
    quantity: Int,
    form: Int,
    forms: Array<String>,
    onDelete: (LocalTime) -> Unit,
    onTimeClick: () -> Unit = {},
    onDoseClick: () -> Unit = {},
) {
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
                        interactionSource = MutableInteractionSource()
                    ) { onDelete(time) }
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
            Text(
                text = "$quantity, ${forms[form]}",
                style = Typography.bodyMedium,
                modifier = Modifier.clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = onDoseClick
                )
            )
            Spacer(Modifier.width(0.dp))
        }
    }
}