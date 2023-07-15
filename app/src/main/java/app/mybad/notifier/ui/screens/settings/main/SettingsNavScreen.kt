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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.mybad.notifier.ui.screens.reuse.TitleText
import app.mybad.notifier.ui.screens.settings.SettingsViewModel
import app.mybad.notifier.ui.screens.settings.common.BaseDivider
import app.mybad.notifier.ui.screens.settings.common.SettingsItem
import app.mybad.notifier.ui.screens.settings.common.UserImage
import app.mybad.theme.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SettingsNavScreen(
    onProfileClicked: () -> Unit = {},
    onNotificationsClicked: () -> Unit = {},
    onAboutClicked: () -> Unit = {},
    onWishesClicked: () -> Unit = {},
    onBackPressed: () -> Unit = {}
) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val editAvatar = remember { mutableStateOf(state.personalDomainModel.avatar) }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { TitleText(textStringRes = R.string.navigation_settings_main) }
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserImage(
                url = state.personalDomainModel.avatar,
                showEdit = true
            ) {
                editAvatar.value = it
//                reducer.invoke(
//                    SettingsIntent.SetPersonal(
//                        userModel.copy(
//                            name = userModel.name,
//                            avatar = editAvatar.value,
//                            email = userModel.email
//                        )
//                    )
//                )
            }
            Spacer(Modifier.height(32.dp))
            SettingsItem(
                label = stringResource(R.string.settings_profile),
                icon = R.drawable.icon_settings_user,
                onSelect = onProfileClicked
            )
            BaseDivider()
            SettingsItem(
                label = stringResource(R.string.settings_notifications),
                icon = R.drawable.icon_settings_notifications,
                onSelect = onNotificationsClicked
            )
            BaseDivider()
            SettingsItem(
                label = stringResource(R.string.settings_leave_your_wishes),
                icon = R.drawable.icon_settings_help,
                onSelect = onWishesClicked
            )
            BaseDivider()

            SettingsItem(
                label = stringResource(R.string.settings_about),
                icon = R.drawable.icon_settings_information,
                onSelect = onAboutClicked
            )
            BaseDivider()
        }
    }

}
