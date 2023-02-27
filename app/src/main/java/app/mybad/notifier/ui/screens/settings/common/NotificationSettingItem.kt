package app.mybad.notifier.ui.screens.settings.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.theme.Typography

@Composable
fun NotificationSettingItem(
    modifier: Modifier = Modifier,
    isChecked: Boolean = true,
    label: String = "label",
    description: String = "description of setting",
    onSwitch: (Boolean) -> Unit = {}
) {

    var checked by remember { mutableStateOf(isChecked) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = label, style = Typography.bodyLarge)
            Text(text = description, style = Typography.labelMedium.copy(), modifier = Modifier.alpha(0.6f))
        }
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = !checked
                onSwitch(it)
            },
            modifier = Modifier.padding(start = 32.dp)
        )
    }

}