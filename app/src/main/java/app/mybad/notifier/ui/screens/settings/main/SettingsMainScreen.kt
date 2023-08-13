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
import app.mybad.notifier.ui.base.SIDE_EFFECTS_KEY
import app.mybad.notifier.ui.common.TitleText
import app.mybad.notifier.ui.screens.settings.common.BaseHorizontalDivider
import app.mybad.notifier.ui.screens.settings.common.SettingsItem
import app.mybad.notifier.ui.screens.settings.common.UserImage
import app.mybad.theme.R
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMainScreen(
    state: SettingsMainContract.State,
    effectFlow: Flow<SettingsMainContract.Effect>? = null,
    sendEvent: (event: SettingsMainContract.Event) -> Unit = {},
    navigation: (navigationEffect: SettingsMainContract.Effect.Navigation) -> Unit
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.collect { effect ->
            when (effect) {
                is SettingsMainContract.Effect.Navigation -> navigation(effect)
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
                onSelect = { sendEvent(SettingsMainContract.Event.ProfileClicked) }
            )
            BaseHorizontalDivider()
            SettingsItem(
                label = stringResource(R.string.settings_notifications),
                icon = R.drawable.icon_settings_notifications,
                onSelect = { sendEvent(SettingsMainContract.Event.NotificationsSettingsClicked) }
            )
            BaseHorizontalDivider()
            SettingsItem(
                label = stringResource(R.string.settings_leave_your_wishes),
                icon = R.drawable.icon_settings_help,
                onSelect = { sendEvent(SettingsMainContract.Event.LeaveWishesClicked) }
            )
            BaseHorizontalDivider()
            SettingsItem(
                label = stringResource(R.string.settings_about),
                icon = R.drawable.icon_settings_information,
                onSelect = { sendEvent(SettingsMainContract.Event.AboutClicked) }
            )
            BaseHorizontalDivider()
            // очистить базы данных
            SettingsItem(
                label = stringResource(R.string.settings_db_clear),
                icon = R.drawable.delete_forever_24,
                onSelect = { sendEvent(SettingsMainContract.Event.ClearDB) }
            )
            BaseHorizontalDivider()
        }
    }

}
