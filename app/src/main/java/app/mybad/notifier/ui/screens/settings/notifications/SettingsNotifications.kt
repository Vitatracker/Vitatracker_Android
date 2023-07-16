package app.mybad.notifier.ui.screens.settings.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.user.NotificationSettingDomainModel
import app.mybad.notifier.ui.screens.settings.common.NotificationSettingItem
import app.mybad.theme.R

@Composable
fun SettingsNotifications(
    modifier: Modifier = Modifier,
    init: NotificationSettingDomainModel = NotificationSettingDomainModel(),
    onSwitch: (NotificationSettingDomainModel) -> Unit = {}
) {
    var newNotifications by remember { mutableStateOf(init) }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Spacer(Modifier.height(16.dp))
        NotificationSettingItem(
            label = stringResource(R.string.settings_enable_notifications),
            description = stringResource(R.string.settings_enable_notifications_description),
            isChecked = newNotifications.isEnabled
        ) {
            newNotifications = newNotifications.copy(isEnabled = it)
            onSwitch(newNotifications)
        }
        Divider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )

        NotificationSettingItem(
            label = stringResource(R.string.settings_enable_float_notifications),
            description = stringResource(R.string.settings_enable_float_notifications_description),
            isChecked = newNotifications.isFloat
        ) {
            newNotifications = newNotifications.copy(isFloat = it)
            onSwitch(newNotifications)
        }
        Divider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )

        NotificationSettingItem(
            label = stringResource(R.string.settings_enable_medication_control),
            description = stringResource(R.string.settings_enable_medication_control_description),
            isChecked = newNotifications.medicationControl
        ) {
            newNotifications = newNotifications.copy(medicationControl = it)
            onSwitch(newNotifications)
        }
        Divider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        )

        NotificationSettingItem(
            label = stringResource(R.string.settings_enable_next_course_start),
            description = stringResource(R.string.settings_enable_next_course_start_description),
            isChecked = newNotifications.nextCourseStart
        ) {
            newNotifications = newNotifications.copy(nextCourseStart = it)
            onSwitch(newNotifications)
        }
    }
}
