package app.mybad.notifier.ui.common

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.theme.R

@Composable
fun ReUseAlertDialog(
    @StringRes titleId: Int? = null,
    @DrawableRes drawableId: Int? = null,
    @StringRes textId: Int? = null,

    textButton: Boolean = true,
    onClickOutside: (() -> Unit)? = null,

    @StringRes firstId: Int? = null,
    firstErrorColor: Boolean = false,
    onClickFirst: () -> Unit = {},

    @StringRes secondId: Int,
    onClickSecond: () -> Unit = {},
) {

    AlertDialog(
        onDismissRequest = {
            onClickOutside?.invoke()
        },
        title = {
            titleId?.let {
                Text(
                    text = stringResource(it),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Start,
                )
            }
        },
        text = {
            Column {
                drawableId?.let {
                    Image(
                        painter = painterResource(it),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(42.dp))
                }
                textId?.let {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(textId),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = if (titleId == null) TextAlign.Center else TextAlign.Justify,
                    )
                }
            }
        },
        dismissButton = {
            firstId?.let {
                if (textButton) {
                    ReUseTextButton(
                        textId = firstId,
                        onClick = onClickFirst,
                    )
                } else {
                    ReUseOutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth(0.48f),
                        errorColor = firstErrorColor,
                        textId = firstId,
                        onClick = onClickFirst,
                    )
                }
            }
        },
        confirmButton = {
            if (textButton) {
                ReUseTextButton(
                    textId = secondId,
                    onClick = onClickSecond,
                )
            } else {
                ReUseFilledButton(
                    modifier = Modifier
                        .fillMaxWidth(0.48f),
                    textId = secondId,
                    onClick = onClickSecond,
                )
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = onClickOutside != null,
            dismissOnClickOutside = onClickOutside != null,
            securePolicy = SecureFlagPolicy.SecureOn,
        ),
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        titleContentColor = if (textId == null) MaterialTheme.colorScheme.onSurfaceVariant
        else MaterialTheme.colorScheme.onPrimary,
        textContentColor = if (titleId == null) MaterialTheme.colorScheme.onSurfaceVariant
        else MaterialTheme.colorScheme.onPrimary,
    )
}

@Preview(
    showBackground = false,
    widthDp = 720, heightDp = 1080,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "ReUseFilledButtonPreviewDark"
)
@Composable
private fun ReUseFilledButtonPreview() {
    MyBADTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ReUseAlertDialog(
                textButton = false,
                titleId = R.string.settings_notifications_allow_reconfirmation_title,
                textId = R.string.settings_notifications_allow_reconfirmation_text,
                firstId = R.string.settings_notifications_allow_reconfirmation_button_dismiss,
                secondId = R.string.settings_notifications_allow_reconfirmation_button_confirm,
            )
        }
    }
}
