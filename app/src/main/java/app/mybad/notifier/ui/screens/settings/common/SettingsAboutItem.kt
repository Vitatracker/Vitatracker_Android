package app.mybad.notifier.ui.screens.settings.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.theme.Typography

@Composable
fun SettingsAboutItem(
   modifier: Modifier = Modifier,
   label: String = "label",
   summary: String = "summary",
   text: String = "text"
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = label, style = Typography.headlineLarge)
        Spacer(Modifier.height(8.dp))
        Text(text = summary, style = Typography.bodyLarge)
        Spacer(Modifier.height(8.dp))
        Text(text = text, style = Typography.bodyMedium)
    }
}