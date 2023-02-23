package app.mybad.notifier.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.notifier.ui.screens.settings.about.SettingsAbout
import app.mybad.notifier.ui.screens.settings.notifications.SettingsNotifications
import app.mybad.notifier.ui.screens.settings.profile.SettingsPasswordEdit
import app.mybad.notifier.ui.screens.settings.profile.SettingsProfile
import app.mybad.notifier.ui.screens.settings.profile.SettingsProfileEdit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsNav(
    modifier: Modifier = Modifier,
    userModel: UserDomainModel = UserDomainModel(),
    navController: NavHostController,
) {

    var title by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(
                text = title,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp)
            ) },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = null,
                    modifier = Modifier
                        .clickable { navController.popBackStack() }
                        .clip(CircleShape)
                )
            },
        )
        NavHost(
            navController = navController,
            startDestination = NavItemSettings.Navigation.route
        ) {
            composable(NavItemSettings.Navigation.route) {
                title = stringResource(NavItemSettings.Navigation.stringId)
                SettingsNavScreen(
                    userModel = userModel,
                    onAbout = { navController.navigate(NavItemSettings.About.route) },
                    onProfile = { navController.navigate(NavItemSettings.Profile.route) },
                    onNotifications = { navController.navigate(NavItemSettings.Notifications.route) },
                    onDelete = {},
                    onQuit = {}
                )
            }
            composable(NavItemSettings.Profile.route) {
                title = stringResource(NavItemSettings.Profile.stringId)
                SettingsProfile(
                    userModel = userModel,
                    onAvatarEdit = { navController.navigate(NavItemSettings.ProfileEdit.route) },
                    onPasswordEdit = { navController.navigate(NavItemSettings.PasswordChange.route) }
                )
            }
            composable(NavItemSettings.ProfileEdit.route) {
                title = stringResource(NavItemSettings.ProfileEdit.stringId)
                SettingsProfileEdit(
                    userModel = userModel,
                    onSave = { navController.popBackStack(NavItemSettings.Profile.route, true) },
                    onDismiss = { navController.popBackStack(NavItemSettings.Profile.route, true) },
                )
            }
            composable(NavItemSettings.PasswordChange.route) {
                title = stringResource(NavItemSettings.PasswordChange.stringId)
                SettingsPasswordEdit(
                    userModel = userModel,
                    onSave = { navController.popBackStack(NavItemSettings.Profile.route, true) },
                    onDismiss = { navController.popBackStack(NavItemSettings.Profile.route, true) },
                )
            }
            composable(NavItemSettings.Notifications.route) {
                title = stringResource(NavItemSettings.Notifications.stringId)
                SettingsNotifications()
            }
            composable(NavItemSettings.About.route) {
                title = stringResource(NavItemSettings.About.stringId)
                SettingsAbout()
            }
        }
    }
}