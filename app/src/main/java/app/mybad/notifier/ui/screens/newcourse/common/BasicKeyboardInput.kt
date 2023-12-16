package app.mybad.notifier.ui.screens.newcourse.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign

@Composable
fun BasicKeyboardInput(
    @StringRes label: Int,
    init: String? = null,
    modifier: Modifier = Modifier.fillMaxWidth(),
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.Words,
    keyboardType: KeyboardType = KeyboardType.Text,
    hideOnGo: Boolean = false,
    prefix: @Composable () -> Unit = {},
    alignRight: Boolean = false,
    onChange: (String) -> Unit = {}
) {
    val lfm = LocalFocusManager.current
    BasicTextField(
        value = init ?: "",
        onValueChange = { onChange(it) },
        singleLine = true,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            imeAction = if (hideOnGo) ImeAction.Go else ImeAction.Next,
            keyboardType = keyboardType,
            capitalization = capitalization
        ),
        keyboardActions = KeyboardActions(onGo = { lfm.clearFocus(true) }),
        textStyle = style.copy(
            textAlign = if (alignRight) TextAlign.End else TextAlign.Start,
            color = MaterialTheme.colorScheme.onPrimary,
        ),
    ) { innerTextField ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            prefix()
            Box(
                contentAlignment = if (alignRight) Alignment.CenterEnd else Alignment.CenterStart
            ) {
                if (init.isNullOrBlank()) {
                    Text(
                        text = stringResource(id = label),
                        modifier = Modifier.alpha(0.3f),
                        style = style.copy(
                            textAlign = if (alignRight) TextAlign.End else TextAlign.Start,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                        ),
                    )
                }
                innerTextField()
            }
        }
    }
}
