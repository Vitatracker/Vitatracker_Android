package app.mybad.notifier.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircle
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.theme.Typography
import app.mybad.notifier.utils.toText
import app.mybad.utils.displayTimeInMinutes

@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    time: Int, // с учетом часового пояса
    quantity: Float,
    form: Int,
    forms: Array<String>,
    onDelete: () -> Unit = {},
    onTimeClick: () -> Unit = {},
    onDoseChange: (Float) -> Unit = {}
) {
    val fm = LocalFocusManager.current
    var field by remember { mutableStateOf(TextFieldValue(quantity.toText())) }

    var isDone by remember { mutableStateOf(false) }
    LaunchedEffect(isDone) {
        field = field.copy(text = quantity.toText())
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiaryContainer),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
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
            Text(
                modifier = Modifier
                    .clickable(onClick = onTimeClick),
                text = time.displayTimeInMinutes(), // отображает время как есть
                style = Typography.bodyLarge,
            )
            Row(modifier = Modifier.weight(0.4f), horizontalArrangement = Arrangement.End) {
                BasicTextField(
                    modifier = Modifier
                        .widthIn(min = 25.dp, max = 100.dp)
                        .onFocusChanged {
                            if (it.hasFocus || it.isFocused) {
                                field = field.copy(selection = TextRange(0, field.text.length))
                            }
                        },
                    value = field,
                    textStyle = TextStyle.Default.copy(textAlign = TextAlign.End),
                    onValueChange = {
                        field = it.copy(
                            text = it.text
                                .replace(",", ".")
                                .replace("-", "")
                                .replace("+", "")
                        )
                        onDoseChange(field.text.toFloatOrNull() ?: 1f)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            fm.clearFocus(true)
                            field = field.copy(selection = TextRange.Zero)
                            isDone = !isDone
                        }
                    )
                )
                Text(
                    text = forms[form],
                    style = Typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun NotificationItemPreview() {
    NotificationItem(
        time = 140,
        quantity = 1.12345f,
        form = 0,
        forms = arrayOf("Таблетка"),
    )
}
