package app.mybad.notifier.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.mybad.notifier.ui.theme.MyBADTheme
import app.mybad.theme.R

@Composable
fun ReUseFilledButton(
    @StringRes textId: Int,
    modifier: Modifier = Modifier.fillMaxWidth(),
    isEnabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    Button(
        modifier = modifier,
        enabled = isEnabled,
        onClick = onClick,
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = stringResource(id = textId),
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 16.sp
        )
    }
}

@Composable
fun ReUseOutlinedButton(
    @StringRes textId: Int,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    OutlinedButton(
        modifier = modifier,
        enabled = isEnabled,
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Gray
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = stringResource(id = textId),
            fontSize = 16.sp
        )
    }
}

@Preview
@Composable
fun ReUseButtonContinuePreview() {
    MyBADTheme {
        ReUseFilledButton(
            modifier = Modifier
                .width(45.dp)
                .height(50.dp),
            textId = R.string.text_continue
        )
    }
}
