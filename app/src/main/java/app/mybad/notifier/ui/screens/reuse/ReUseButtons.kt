package app.mybad.notifier.ui.screens.reuse

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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

@Composable
fun ReUseFilledButton(textId: Int, isEnabled: Boolean = true, onClick: () -> Unit = {}) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = isEnabled,
        onClick = {
            onClick()
        },
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
fun ReUseOutlinedButton(textId: Int, onClick: () -> Unit = {}) {
    OutlinedButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick() },
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
            modifier = Modifier,
            fontSize = 16.sp
        )
    }
}

@Preview
@Composable
fun ReUseButtonContinuePreview() {
    MyBADTheme {
        ReUseFilledButton(textId = app.mybad.theme.R.string.text_continue)
    }
}
