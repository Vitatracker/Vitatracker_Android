package app.mybad.notifier.ui.screens.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import app.mybad.notifier.ui.screens.settings.SettingsIntent
import app.mybad.notifier.ui.screens.settings.SettingsViewModel
import app.mybad.notifier.ui.screens.settings.profile.SettingsProfile

fun NavGraphBuilder.profileNavGraph(navigationState: NavigationState) {
    navigation(startDestination = ProfileScreens.MainScreen.route, route = Screen.Profile.route) {
        composable(route = ProfileScreens.MainScreen.route) {
            val viewModel: SettingsViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            SettingsProfile(
                userModel = state.personalDomainModel,
                savePersonal = {},
                onChangePasswordClicked = {},
                onChangeAccountClicked = {
                    viewModel.reduce(SettingsIntent.Exit)
                },
                onDeleteAccountClicked = {},
                onBackPressed = {
                    navigationState.navController.popBackStack()
                }
            )
        }
    }
}