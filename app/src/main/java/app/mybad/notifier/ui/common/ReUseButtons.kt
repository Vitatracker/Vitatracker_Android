package app.mybad.notifier.ui.common

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.notifier.ui.theme.Typography
import app.mybad.theme.R

@Composable
fun ReUseFilledButton(
    @StringRes textId: Int,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    Button(
        modifier = modifier
        .heightIn(min = 52.dp, max = 100.dp),
        enabled = enabled,
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
    ) {
        Text(
            text = stringResource(id = textId),
            style = Typography.labelLarge,
        )
    }
}

@Composable
fun ReUseOutlinedButton(
    @StringRes textId: Int,
    modifier: Modifier = Modifier.fillMaxWidth(),
    errorColor: Boolean = false,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    OutlinedButton(
        modifier = modifier
            .heightIn(min = 52.dp, max = 100.dp),
        enabled = enabled,
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (errorColor) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.secondary,
            contentColor = if (errorColor) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSecondary,
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (errorColor) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        ),
    ) {
        Text(
            text = stringResource(id = textId),
            style = Typography.labelLarge,
            fontWeight = FontWeight.Normal,
        )
    }
}

@Composable
fun ReUseTextButton(
    @StringRes textId: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(text = stringResource(textId), style = Typography.labelMedium)
    }
}

@Composable
fun ReUseTwoButtons(
    @StringRes dismissId: Int = R.string.settings_cancel,
    @StringRes confirmId: Int = R.string.settings_save,
    modifier: Modifier = Modifier.fillMaxWidth(),
    errorColor: Boolean = false,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier,
    ) {
        ReUseOutlinedButton(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            textId = dismissId,
            errorColor = errorColor,
            onClick = onDismiss,
        )
        Spacer(Modifier.width(16.dp))
        ReUseFilledButton(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            textId = confirmId,
            onClick = onConfirm,
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 320, heightDp = 400,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "ReUseTwoButtonsPreviewDark"
)
@Composable
private fun ReUseTwoButtonsPreview() {
    MyBADTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ReUseTwoButtons(errorColor = true)
        }
    }
}

@Preview(
    showBackground = false,
    widthDp = 320, heightDp = 400,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "ReUseFilledButtonPreviewDark"
)
@Composable
private fun ReUseFilledButtonPreview() {
    MyBADTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ReUseFilledButton(
                textId = R.string.text_continue
            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320, heightDp = 400,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "ReUseOutlinedButtonPreviewDark"
)
@Composable
private fun ReUseOutlinedButtonPreview() {
    MyBADTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ReUseOutlinedButton(
                textId = R.string.text_continue
            )
        }
    }
}
