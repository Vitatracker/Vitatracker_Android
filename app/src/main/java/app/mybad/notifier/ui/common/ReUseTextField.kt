package app.mybad.notifier.ui.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    modifier: Modifier = Modifier,
    value: String = "",
    @StringRes placeholder: Int,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorTextId: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    @DrawableRes trailingIcon: Int? = null,
    tint: Color = LocalContentColor.current,
    onValueChanged: (String) -> Unit = {},
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue -> onValueChanged(newValue) },
        modifier = modifier.fillMaxWidth(),
        isError = isError,
        enabled = enabled,
        supportingText = {
            if (errorTextId != null) {
                Text(text = stringResource(id = errorTextId))
            }
        },
        singleLine = singleLine,
        trailingIcon = {
            if (trailingIcon != null || errorTextId != null) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = if (errorTextId != null) Icons.Default.Error
                    else ImageVector.vectorResource(trailingIcon!!),
                    contentDescription = null,
                    tint = tint
                )
            }
        },
        placeholder = { Text(stringResource(placeholder)) },
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            disabledBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(0.38f),

            cursorColor = MaterialTheme.colorScheme.onPrimary,

            errorBorderColor = MaterialTheme.colorScheme.errorContainer,

            focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceDim,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceDim,
            disabledPlaceholderColor = MaterialTheme.colorScheme.surfaceDim.copy(0.38f),
        )
    )
}

@Composable
fun ReUsePasswordOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    @StringRes placeholder: Int,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorTextId: Int? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done
    ),
    onValueChanged: (String) -> Unit = {},
) {
    var showPassword by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier
            .fillMaxWidth(),
        singleLine = true,
        isError = isError,
        enabled = enabled,
        supportingText = { if (errorTextId != null) Text(stringResource(errorTextId)) },
        placeholder = { Text(stringResource(placeholder)) },
        keyboardOptions = keyboardOptions,
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }) {
                Icon(
                    if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = null,
                    tint = Color.Black,
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            disabledBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(0.38f),

            cursorColor = MaterialTheme.colorScheme.onPrimary,

            errorBorderColor = MaterialTheme.colorScheme.errorContainer,

            focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceDim,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceDim,
            disabledPlaceholderColor = MaterialTheme.colorScheme.surfaceDim.copy(0.38f),
        )
    )
}
