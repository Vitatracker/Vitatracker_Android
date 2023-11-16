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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R

@Composable
fun ReUseAlertDialog(
    @StringRes titleId: Int,
    @DrawableRes drawableId: Int? = null,
    @StringRes textId: Int,

    textButton: Boolean = true,
    dismissOnClickOutside: Boolean = false,

    @StringRes dismissId: Int? = null,
    onDismiss: () -> Unit = {},
    @StringRes confirmId: Int,
    onConfirm: () -> Unit = {},
) {

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(
                text = stringResource(titleId),
                style = Typography.titleMedium,
                textAlign = TextAlign.Start
            )
        },
        text = {
            Column {
                drawableId?.let {
                    Image(painter = painterResource(it), contentDescription = null)
                    Spacer(modifier = Modifier.height(42.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(textId),
                    style = Typography.bodyMedium,
                    textAlign = TextAlign.Justify
                )
            }
        },
        dismissButton = {
            dismissId?.let {
                if (textButton) {
                    ReUseTextButton(
                        textId = dismissId,
                        onClick = onDismiss,
                    )
                } else {
                    ReUseOutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth(0.48f),
                        errorColor = true,
                        textId = dismissId,
                        onClick = onDismiss,
                    )
                }
            }
        },
        confirmButton = {
            if (textButton) {
                ReUseTextButton(
                    textId = confirmId,
                    onClick = onConfirm,
                )
            } else {
                ReUseFilledButton(
                    modifier = Modifier
                        .fillMaxWidth(0.48f),
                    textId = confirmId,
                    onClick = onConfirm,
                )
            }
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
                dismissId = R.string.settings_notifications_allow_reconfirmation_button_dismiss,
                confirmId = R.string.settings_notifications_allow_reconfirmation_button_confirm,
            )
        }
    }
}

/*
@Composable
fun ReUseAlertDialogWithPicture(
    @StringRes titleId: Int,
    @StringRes descriptionId: Int,
    dismissOnClickOutside: Boolean = false,
    @StringRes dismissId: Int,
    onDismiss: () -> Unit,
    @StringRes confirmId: Int,
    onConfirm: () -> Unit,

    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    dismissButton: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    iconContentColor: Color = AlertDialogDefaults.iconContentColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    properties: DialogProperties = DialogProperties()
) {
    ReUseAlertDialogContent(

    )
}

@Composable
private fun ReUseAlertDialogContent(
    buttons: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)?,
    title: (@Composable () -> Unit)?,
    text: @Composable (() -> Unit)?,
    shape: Shape,
    containerColor: Color,
    tonalElevation: Dp,
    buttonContentColor: Color,
    iconContentColor: Color,
    titleContentColor: Color,
    textContentColor: Color,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = containerColor,
        tonalElevation = tonalElevation,
    ) {
        Column(
            modifier = Modifier.padding(DialogPadding)
        ) {
            icon?.let {
                CompositionLocalProvider(LocalContentColor provides iconContentColor) {
                    Box(
                        Modifier
                            .padding(IconPadding)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        icon()
                    }
                }
            }
            title?.let {
                ProvideContentColorTextStyle(
                    contentColor = titleContentColor,
                    textStyle = MaterialTheme.typography.headlineSmall
                ) {
                    Box(
                        // Align the title to the center when an icon is present.
                        Modifier
                            .padding(TitlePadding)
                            .align(
                                if (icon == null) {
                                    Alignment.Start
                                } else {
                                    Alignment.CenterHorizontally
                                }
                            )
                    ) {
                        title()
                    }
                }
            }
            text?.let {
                val textStyle = MaterialTheme.typography.bodyMedium
                ProvideContentColorTextStyle(
                    contentColor = textContentColor,
                    textStyle = textStyle) {
                    Box(
                        Modifier
                            .weight(weight = 1f, fill = false)
                            .padding(TextPadding)
                            .align(Alignment.Start)
                    ) {
                        text()
                    }
                }
            }
            Box(modifier = Modifier.align(Alignment.End)) {
                val textStyle = MaterialTheme.typography.labelLarge
                ProvideContentColorTextStyle(
                    contentColor = buttonContentColor,
                    textStyle = textStyle,
                    content = buttons)
            }
        }
    }
}

internal val DialogMinWidth = 280.dp
internal val DialogMaxWidth = 560.dp

// Paddings for each of the dialog's parts.
private val DialogPadding = PaddingValues(all = 24.dp)
private val IconPadding = PaddingValues(bottom = 16.dp)
private val TitlePadding = PaddingValues(bottom = 16.dp)
private val TextPadding = PaddingValues(bottom = 24.dp)
*/
