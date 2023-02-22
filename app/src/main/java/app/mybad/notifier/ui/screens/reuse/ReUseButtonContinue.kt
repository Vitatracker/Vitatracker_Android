package app.mybad.notifier.ui.screens.reuse

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.mybad.notifier.R

@Composable
fun ReUseButtonContinue(textId: Int, onClick: () -> Unit) {

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 8.dp, end = 8.dp),
        onClick = { onClick() },
        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Text(text = stringResource(id = textId))
    }

}