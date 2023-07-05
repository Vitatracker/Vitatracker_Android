package app.mybad.notifier.ui.screens.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.theme.Typography

@Composable
fun StylishTextBox(
    modifier: Modifier = Modifier,
    label: String?,
    value: String?,
    readOnly: Boolean = true,
    outlined: Boolean = true,
    @DrawableRes icon: Int? = null,
    minLines: Int = 1,
    onChange: (String) -> Unit,
    onIconClick: () -> Unit = {},
) {
    val lfm = LocalFocusManager.current
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(
            1.dp,
            if (outlined) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BasicTextField(
                value = value ?: "",
                onValueChange = onChange::invoke,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { lfm.clearFocus(true) }),
                minLines = minLines,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .weight(1f),
                readOnly = readOnly
            ) { innerField ->
                Box {
                    if (value.isNullOrBlank() && !label.isNullOrBlank()) {
                        Text(
                            text = label,
                            style = Typography.bodyLarge,
                            color = Color.Unspecified.copy(alpha = 0.6f)
                        )
                    }
                    innerField()
                }
            }
            if (icon != null) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(24.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },

                            onClick = onIconClick::invoke
                        )
                )
            }
        }
    }
}
