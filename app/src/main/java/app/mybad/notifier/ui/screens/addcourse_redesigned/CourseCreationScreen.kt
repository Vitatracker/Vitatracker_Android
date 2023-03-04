package app.mybad.notifier.ui.screens.addcourse_redesigned

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.addcourse_redesigned.common.BasicKeyboardInput
import app.mybad.notifier.ui.screens.addcourse_redesigned.common.ModalDateSelector
import app.mybad.notifier.ui.screens.addcourse_redesigned.common.ModalInteractionSelector
import app.mybad.notifier.ui.theme.Typography
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseCreationScreen(
    modifier: Modifier = Modifier,
    startTime: LocalDateTime,
    endTime: LocalDateTime,
    regime: Int,
    onSetStart: (LocalDateTime) -> Unit,
    onSetEnd: (LocalDateTime) -> Unit,
    onSetRegime: () -> Unit,
    onSetComment: (String) -> Unit,
    onSetReminder: () -> Unit
) {
    val pickerState = rememberDatePickerState()
    var datePickerShown by remember { mutableStateOf(false) }
    var datePickerType by remember { mutableStateOf(SelectionType.START_DATE) }

    Column {
        Text(
            text = stringResource(R.string.mycourse_duration),
            style = Typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                ModalDateSelector(
                    modifier = Modifier.padding(16.dp),
                    selected = startTime,
                    label = stringResource(R.string.add_course_start_time),
                    onClick = {
                        datePickerShown = true
                        datePickerType = SelectionType.START_DATE
                    }
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                ModalDateSelector(
                    modifier = Modifier.padding(16.dp),
                    selected = endTime,
                    label = stringResource(R.string.add_course_start_time),
                    onClick = {
                        datePickerShown = true
                        datePickerType = SelectionType.END_DATE
                    }
                )
                Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.primaryContainer)
                ModalInteractionSelector(
                    modifier = Modifier.padding(16.dp),
                    selected = regime,
                    label = stringResource(R.string.medication_regime),
                    items = stringArrayResource(R.array.regime).toList(),
                    onClick = onSetRegime::invoke
                )
            }
        }
        Text(
            text = stringResource(R.string.add_course_comment),
            style = Typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Surface(
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
            color = MaterialTheme.colorScheme.background,
            modifier = modifier
                .fillMaxWidth()
        ) {
            BasicKeyboardInput(
                modifier = Modifier.padding(16.dp),
                label = stringResource(R.string.add_course_comment),
                hideOnGo = true,
                capitalization = KeyboardCapitalization.Sentences,
                onChange = { onSetComment(it) }
            )
        }
        Button(
            onClick = onSetReminder::invoke,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(R.drawable.clock),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(20.dp)
            )
            Text(
                text = stringResource(R.string.add_course_reminder),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
    if(datePickerShown) {
        DatePickerDialog(
            onDismissRequest = { datePickerShown = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerShown = false
                        when(datePickerType) {
                            SelectionType.START_DATE -> {
                                onSetStart(
                                    LocalDateTime.ofInstant(
                                        Instant.ofEpochMilli(pickerState.selectedDateMillis ?: 0L
                                        ), ZoneId.systemDefault())
                                )
                            }
                            SelectionType.END_DATE -> {
                                onSetEnd(
                                    LocalDateTime.ofInstant(
                                        Instant.ofEpochMilli(pickerState.selectedDateMillis ?: 0L
                                        ), ZoneId.systemDefault())
                                )
                            }
                        }
                    },
                    content = { Text(text = stringResource(R.string.settings_save)) }
                )
            },
            dismissButton = {
                Button(
                    onClick = { datePickerShown = false },
                    content = { Text(text = stringResource(R.string.settings_cancel)) }
                )
            }
        ) {
            DatePicker(
                title = { Text(
                    text = stringResource(R.string.add_course_start_time),
                    style = Typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                ) },
                state = pickerState,
                showModeToggle = true,
                dateValidator = { when(datePickerType) {
                    SelectionType.START_DATE -> it/1000 > LocalDateTime.now().withHour(0).atZone(
                        ZoneId.systemDefault()).toEpochSecond()
                    SelectionType.END_DATE -> it/1000 > startTime.atZone(ZoneId.systemDefault()).toEpochSecond()
                } }
            )
        }
    }
}