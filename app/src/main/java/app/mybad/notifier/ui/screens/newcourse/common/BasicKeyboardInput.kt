package app.mybad.notifier.ui.screens.newcourse.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import app.mybad.notifier.ui.theme.Typography

@Composable
fun BasicKeyboardInput(
    modifier: Modifier = Modifier.fillMaxWidth(),
    init: String? = null,
    label: String,
    style: TextStyle = Typography.bodyMedium,
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
        textStyle = if (!alignRight) style else style.copy(textAlign = TextAlign.End),
    ) { innerTextField ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            prefix()
            Box(
                contentAlignment = if (!alignRight) Alignment.CenterStart else Alignment.CenterEnd
            ) {
                if (init.isNullOrBlank()) {
                    Text(
                        text = label,
                        modifier = Modifier.alpha(0.3f),
                        style = if (!alignRight) {
                            style.copy(fontWeight = FontWeight.Bold)
                        } else {
                            style.copy(textAlign = TextAlign.End, fontWeight = FontWeight.Bold)
                        }
                    )
                }
                innerTextField()
            }
        }
    }
}
