package app.mybad.notifier.ui.screens.reuse

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun ReUseOutlinedTextField(
    value: String = "",
    label: String = "",
    enabled: Boolean = true,
    onValueChanged: (String) -> Unit = {},
    isError: Boolean = false,
    errorTextId: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    trailingIcon: @Composable() (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue -> onValueChanged(newValue) },
        modifier = Modifier.fillMaxWidth(),
        isError = isError,
        enabled = enabled,
        supportingText = {
            if (errorTextId != null) {
                Text(text = stringResource(id = errorTextId))
            }
        },
        singleLine = singleLine,
        trailingIcon = {
            if (trailingIcon != null) {
                trailingIcon()
            } else if (errorTextId != null) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.Error,
                    contentDescription = null
                )
            }
        },
        placeholder = { Text(text = label) },
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
            errorBorderColor = MaterialTheme.colorScheme.errorContainer
        )
    )
}

@Composable
fun OutlinedPasswordTextField(
    value: String = "",
    label: String = "",
    isError: Boolean = false,
    onValueChanged: (String) -> Unit = {},
    errorTextId: Int? = null
) {
    val showPassword = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,
        isError = isError,
        supportingText = {
            if (errorTextId != null) {
                Text(text = stringResource(id = errorTextId))
            }
        },
        placeholder = { Text(text = label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val (icon, iconColor) = if (showPassword.value) {
                Pair(Icons.Filled.Visibility, Color.Black)
            } else {
                Pair(Icons.Filled.VisibilityOff, Color.Black)
            }

            IconButton(onClick = { showPassword.value = !showPassword.value }) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
            errorBorderColor = MaterialTheme.colorScheme.errorContainer
        )
    )
}