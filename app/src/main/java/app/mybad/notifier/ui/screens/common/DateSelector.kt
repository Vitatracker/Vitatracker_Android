package app.mybad.notifier.ui.screens.common

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.mybad.notifier.R
import app.mybad.notifier.ui.theme.Typography
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    modifier: Modifier = Modifier,
    label: String,
    checkActualDate: Boolean = false,
    previousEndDate: Long = 1677366000L,
    onSelect: (Long) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val now = System.currentTimeMillis()
    var future = 0L
    val df = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(now).split(".")
    var value by remember { mutableStateOf( "${df[0]}.${df[1]}.${df[2]}") }
    var isError by remember { mutableStateOf(previousEndDate > future && checkActualDate) }
    val picker = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            val m = if((month+1)<10) "0${month+1}" else "${month+1}"
            value = "$dayOfMonth.${m}.$year"
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(value)
            if (sdf != null) {
                onSelect(sdf.time/1000)
                future = sdf.time/1000
                isError = previousEndDate > future && checkActualDate
            }
        },
        df[2].toIntOrNull() ?: 2000,
        (df[1].toIntOrNull() ?: 2) - 1,
        df[0].toIntOrNull() ?: 1,
    )
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = Typography.bodyLarge
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            isError = isError,
            value = value,
            textStyle = Typography.bodyLarge.copy(textAlign = TextAlign.Center),
            onValueChange = {},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Go
            ),
            keyboardActions = KeyboardActions(
                onGo = { focusManager.clearFocus() }
            ),
            shape = RoundedCornerShape(10.dp),
            prefix = { Text(stringResource(R.string.add_course_date_from)) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            picker.show()
                        }
                )
            }
        )
    }
}