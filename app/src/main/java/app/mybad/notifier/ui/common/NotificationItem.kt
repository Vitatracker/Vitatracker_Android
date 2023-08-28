package app.mybad.notifier.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RemoveCircleOutline
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.theme.Typography
import app.mybad.utils.toTimeDisplay

@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    time: Int,
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
            Text(
                text = time.toTimeDisplay(),
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
