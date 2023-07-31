package app.mybad.notifier.ui.screens.settings.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.mybad.notifier.ui.screens.reuse.TitleText
import app.mybad.notifier.ui.screens.settings.common.BaseDivider
import app.mybad.notifier.ui.screens.settings.common.SettingsItem
import app.mybad.notifier.ui.screens.settings.common.UserImage
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsNavScreen(
    state: SettingsMainScreenContract.State,
    events: Flow<SettingsMainScreenContract.Effect>? = null,
    onEventSent: (event: SettingsMainScreenContract.Event) -> Unit = {},
    onNavigationRequested: (navigationEffect: SettingsMainScreenContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(key1 = true) {
        events?.collect {
            when (it) {
                SettingsMainScreenContract.Effect.Navigation.ToAbout -> {
                    onNavigationRequested(SettingsMainScreenContract.Effect.Navigation.ToAbout)
                }

                SettingsMainScreenContract.Effect.Navigation.ToAvatarEdit -> {
                    onNavigationRequested(SettingsMainScreenContract.Effect.Navigation.ToAvatarEdit)
                }

                SettingsMainScreenContract.Effect.Navigation.ToLeaveWishes -> {
                    onNavigationRequested(SettingsMainScreenContract.Effect.Navigation.ToLeaveWishes)
                }

                SettingsMainScreenContract.Effect.Navigation.ToNotificationsSettings -> {
                    onNavigationRequested(SettingsMainScreenContract.Effect.Navigation.ToNotificationsSettings)
                }

                SettingsMainScreenContract.Effect.Navigation.ToProfile -> {
                    onNavigationRequested(SettingsMainScreenContract.Effect.Navigation.ToProfile)
                }
            }
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { TitleText(textStringRes = R.string.navigation_settings_main) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserImage(
                url = state.userAvatar,
                imageSize = 100.dp,
                showEdit = false
            )
            Spacer(Modifier.height(32.dp))
            SettingsItem(
                label = stringResource(R.string.settings_profile),
                icon = R.drawable.icon_settings_user,
                onSelect = { onEventSent(SettingsMainScreenContract.Event.ProfileClicked) }
            )
            BaseDivider()
            SettingsItem(
                label = stringResource(R.string.settings_notifications),
                icon = R.drawable.icon_settings_notifications,
                onSelect = { onEventSent(SettingsMainScreenContract.Event.NotificationsSettingsClicked) }
            )
            BaseDivider()
            SettingsItem(
                label = stringResource(R.string.settings_leave_your_wishes),
                icon = R.drawable.icon_settings_help,
                onSelect = { onEventSent(SettingsMainScreenContract.Event.LeaveWishesClicked) }
            )
            BaseDivider()

            SettingsItem(
                label = stringResource(R.string.settings_about),
                icon = R.drawable.icon_settings_information,
                onSelect = { onEventSent(SettingsMainScreenContract.Event.AboutClicked) }
            )
            BaseDivider()
        }
    }

}
