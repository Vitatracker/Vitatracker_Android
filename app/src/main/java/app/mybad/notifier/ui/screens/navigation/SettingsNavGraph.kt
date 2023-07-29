package app.mybad.notifier.ui.screens.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import app.mybad.notifier.ui.screens.settings.main.SettingsMainScreenContract
import app.mybad.notifier.ui.screens.settings.main.SettingsMainViewModel
import app.mybad.notifier.ui.screens.settings.main.SettingsNavScreen

fun NavGraphBuilder.settingsNavGraph(navigationState: NavigationState) {
    navigation(startDestination = SettingsScreens.Navigation.route, route = MainScreens.Settings.route) {
        composable(route = SettingsScreens.Navigation.route) {
            val viewModel: SettingsMainViewModel = hiltViewModel()
            SettingsNavScreen(
                state = viewModel.viewState.value,
                events = viewModel.effect,
                onEventSent = {
                    viewModel.setEvent(it)
                },
                onNavigationRequested = { navigationEffect ->
                    when (navigationEffect) {
                        SettingsMainScreenContract.Effect.Navigation.ToProfile -> {
                            navigationState.navigateSingleTo(SettingsScreens.Profile.route)
                        }

                        SettingsMainScreenContract.Effect.Navigation.ToNotificationsSettings -> TODO()
                        SettingsMainScreenContract.Effect.Navigation.ToAbout -> TODO()
                        SettingsMainScreenContract.Effect.Navigation.ToAvatarEdit -> TODO()
                        SettingsMainScreenContract.Effect.Navigation.ToLeaveWishes -> TODO()
                    }
                }
            )
        }
        composable(route = SettingsScreens.Profile.route) {

        }
        composable(route = SettingsScreens.Notifications.route) {

        }
        composable(SettingsScreens.LeaveYourWishes.route) {

        }
        composable(SettingsScreens.About.route) {

        }
    }
}
