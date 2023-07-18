package app.mybad.notifier.ui.screens.reuse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.mybad.notifier.ui.theme.MyBADTheme

@Composable
fun Progress() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressPreview() {
    MyBADTheme {
        Progress()
    }
}
