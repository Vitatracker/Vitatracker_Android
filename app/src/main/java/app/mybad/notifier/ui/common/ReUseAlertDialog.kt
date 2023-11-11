package app.mybad.notifier.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import app.mybad.notifier.ui.theme.Typography

@Composable
fun ReUseAlertDialog(
    @StringRes titleId: Int,
    @StringRes descriptionId: Int,
    dismissOnClickOutside: Boolean = false,
    @StringRes dismissId: Int,
    onDismiss: () -> Unit,
    @StringRes confirmId: Int,
    onConfirm: () -> Unit,
) {

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(text = stringResource(confirmId), style = Typography.labelMedium)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(text = stringResource(dismissId), style = Typography.labelMedium)
            }
        },
        title = {
            Text(
                text = stringResource(titleId),
                style = Typography.titleMedium,
                textAlign = TextAlign.Start
            )
        },
        text = {
            Text(
                text = stringResource(descriptionId),
                style = Typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
        },
        properties = DialogProperties(
            dismissOnBackPress = dismissOnClickOutside,
            dismissOnClickOutside = dismissOnClickOutside
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
    )
}
