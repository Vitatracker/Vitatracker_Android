package app.mybad.notifier.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VerticalSpacerLarge() {
    Spacer(modifier = Modifier.height(32.dp))
}

@Composable
fun VerticalSpacerMedium() {
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun VerticalSpacerSmall() {
    Spacer(modifier = Modifier.height(8.dp))
}
