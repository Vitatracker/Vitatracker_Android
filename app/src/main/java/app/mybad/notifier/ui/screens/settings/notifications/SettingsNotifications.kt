package app.mybad.notifier.ui.screens.settings.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.screens.settings.common.NotificationSettingItem

@Composable
@Preview
fun SettingsNotifications(
    modifier: Modifier = Modifier,
    onSwitch: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(Modifier.height(16.dp))
        NotificationSettingItem()
        Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp)
        NotificationSettingItem(isChecked = false)
        Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp)
        NotificationSettingItem()
    }
}