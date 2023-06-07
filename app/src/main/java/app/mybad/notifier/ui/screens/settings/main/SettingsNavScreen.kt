package app.mybad.notifier.ui.screens.settings.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.notifier.ui.screens.settings.common.SettingsItem
import app.mybad.notifier.ui.screens.settings.common.UserImage
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.settings.SettingsIntent
import app.mybad.notifier.ui.screens.settings.common.BaseDivider
import app.mybad.notifier.ui.screens.settings.common.DeleteAccountItem
import app.mybad.notifier.ui.screens.settings.common.SettingsQuit

@Composable
fun SettingsNavScreen(
    modifier: Modifier = Modifier,
    userModel: PersonalDomainModel = PersonalDomainModel(),
    reducer: (SettingsIntent) -> Unit,
    onProfile: () -> Unit = {},
    onNotifications: () -> Unit = {},
    onAbout: () -> Unit = {},
) {

    val editAvatar = remember { mutableStateOf(userModel.avatar) }

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
                url = userModel.avatar,
                showEdit = true
            ) {
                editAvatar.value = it
                reducer.invoke(
                    SettingsIntent.SetPersonal(
                        userModel.copy(
                            name = userModel.name,
                            avatar = editAvatar.value,
                            email = userModel.email
                        )
                    )
                )
            }
            Spacer(Modifier.height(32.dp))
            SettingsItem(
                label = stringResource(R.string.settings_profile),
                onSelect = onProfile::invoke
            )
            BaseDivider()
            SettingsItem(
                label = stringResource(R.string.settings_notifications),
                onSelect = onNotifications::invoke
            )
            BaseDivider()
            SettingsItem(
                label = stringResource(R.string.settings_about),
                onSelect = onAbout::invoke
            )
            BaseDivider()
            DeleteAccountItem(
                label = stringResource(R.string.settings_delete_account),
                onSelect = { reducer(SettingsIntent.DeleteAccount) }
            )
        }
        SettingsQuit(onQuit = { reducer(SettingsIntent.Exit) })
    }

}