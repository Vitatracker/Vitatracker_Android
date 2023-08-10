package app.mybad.notifier.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.mybad.notifier.MainActivityViewModel
import app.mybad.notifier.ui.screens.settings.about.SettingsOurContacts
import app.mybad.notifier.ui.screens.settings.notifications.SettingsNotifications
import app.mybad.notifier.ui.screens.settings.profile.SettingsPasswordEdit
import app.mybad.notifier.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsNav(
    modifier: Modifier = Modifier,
    vm: SettingsViewModel,
    navController: NavHostController,
    mainVM: MainActivityViewModel,
    onDismiss: () -> Unit = { }
) {
    var title by remember { mutableStateOf("") }
    val state = vm.state.collectAsStateWithLifecycle()
    val userModel = state.value.userModel
    val userPersonalModel = state.value.personalDomainModel
//    val userNotificationModel = state.value.notificationsUserDomainModel
//    val userRulesModel = state.value.rulesUserDomainModel

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 24.dp),
                    color = MaterialTheme.colorScheme.primary,
                    style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            if (navController.currentDestination?.route == NavItemSettings.Navigation.route) onDismiss()
                            navController.popBackStack()
                        }
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
//                SettingsNavScreen(
//                    userModel = userPersonalModel,
//                    reducer = { vm.reduce(it) },
//                    onAbout = { navController.navigate(NavItemSettings.About.route) },
//                    onProfile = { navController.navigate(NavItemSettings.Profile.route) },
//                    onWishes = { navController.navigate(NavItemSettings.LeaveYourWishes.route) },
//                    onNotifications = {
//                        navController.navigate(NavItemSettings.Notifications.route)
//                    },
//                )
            }
            composable(NavItemSettings.Profile.route) {
                title = stringResource(NavItemSettings.Profile.stringId)
//                SettingsProfile(
//                    userModel = userPersonalModel,
//                    savePersonal = {
//                        vm.reduce(
//                            SettingsIntent.SetPersonal(personal = (it as SettingsIntent.SetPersonal).personal)
//                        )
//                    },
//                    onPasswordEdit = {
//                        navController.navigate(NavItemSettings.PasswordChange.route)
//                    },
//                    mainVM = mainVM,
//                    settingsVM = vm,
//                    onDismiss = { navController.popBackStack(NavItemSettings.Profile.route, true) }
//                )
            }
            composable(NavItemSettings.LeaveYourWishes.route) {
//                title = stringResource(NavItemSettings.LeaveYourWishes.stringId)
//                SettingsLeaveWishes()
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
//                title = stringResource(NavItemSettings.Notifications.stringId)
//                SettingsNotifications(
//                    init = state.value.userModel.settings.notifications
//                ) {
//                    vm.reduce(SettingsIntent.SetNotifications(it))
//                }
            }
            composable(NavItemSettings.About.route) {
//                title = stringResource(NavItemSettings.About.stringId)
//                SettingsAbout(onContacts = {
//                    navController.navigate(NavItemSettings.OurContacts.route)
//                })
            }
            composable(NavItemSettings.AboutOurTeam.route) {
                title = stringResource(NavItemSettings.AboutOurTeam.stringId)
                SettingsOurContacts()
            }
        }
    }
}
