package app.mybad.notifier.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.notifier.ui.screens.settings.common.SettingsItem
import app.mybad.notifier.ui.screens.settings.common.UserImage
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.settings.common.DeleteAccountItem
import app.mybad.notifier.ui.screens.settings.common.SettingsQuit

@Composable
@Preview(showBackground = true)
fun SettingsNavScreen(
    modifier: Modifier = Modifier,
    userModel: UserDomainModel = UserDomainModel(),
    onProfile: () -> Unit = {},
    onNotifications: () -> Unit = {},
    onAbout: () -> Unit = {},
    onDelete: () -> Unit = {},
    onQuit: () -> Unit = {},
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserImage(
                url = userModel.personal.avatar,
                showEdit = true,
                onEdit = onProfile::invoke
            )
            Spacer(Modifier.height(32.dp))
            SettingsItem(
                label = stringResource(R.string.settings_profile),
                onSelect = onProfile::invoke
            )
            Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp)
            SettingsItem(
                label = stringResource(R.string.settings_notifications),
                onSelect = onNotifications::invoke
            )
            Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp)
            SettingsItem(
                label = stringResource(R.string.settings_about),
                onSelect = onAbout::invoke
            )
            Divider(modifier = Modifier.padding(vertical = 16.dp), thickness = 1.dp)
            DeleteAccountItem(
                label = stringResource(R.string.settings_delete_account),
                onSelect = onAbout::invoke
            )
        }
        SettingsQuit(onQuit = onQuit::invoke)

    }

}