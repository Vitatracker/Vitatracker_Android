package app.mybad.notifier.ui.screens.newcourse.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import app.mybad.notifier.ui.theme.Typography

@Composable
fun SwitchParameterInput(
    modifier: Modifier = Modifier,
    label: String,
    style: TextStyle = Typography.bodyMedium,
    initValue: Boolean = false,
    isActive: Boolean = true,
    onSwitch: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = label, style = style)
        Switch(
            enabled = isActive,
            checked = initValue,
            onCheckedChange = { onSwitch(it) }
        )
    }
}
