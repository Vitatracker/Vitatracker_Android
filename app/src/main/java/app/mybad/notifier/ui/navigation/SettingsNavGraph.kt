package app.mybad.notifier.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import app.mybad.notifier.ui.screens.settings.about.SettingsAboutContract
import app.mybad.notifier.ui.screens.settings.about.SettingsAboutScreen
import app.mybad.notifier.ui.screens.settings.about.SettingsAboutViewModel
import app.mybad.notifier.ui.screens.settings.about_team.SettingsAboutTeamContract
import app.mybad.notifier.ui.screens.settings.about_team.SettingsAboutTeamScreen
import app.mybad.notifier.ui.screens.settings.about_team.SettingsAboutTeamViewModel
import app.mybad.notifier.ui.screens.settings.changepassword.SettingsChangePasswordContract
import app.mybad.notifier.ui.screens.settings.changepassword.SettingsChangePasswordScreen
import app.mybad.notifier.ui.screens.settings.changepassword.SettingsChangePasswordViewModel
import app.mybad.notifier.ui.screens.settings.main.SettingsMainScreen
import app.mybad.notifier.ui.screens.settings.main.SettingsMainScreenContract
import app.mybad.notifier.ui.screens.settings.main.SettingsMainViewModel
import app.mybad.notifier.ui.screens.settings.notifications.SettingsNotificationsContract
import app.mybad.notifier.ui.screens.settings.notifications.SettingsNotificationsScreen
import app.mybad.notifier.ui.screens.settings.notifications.SettingsNotificationsViewModel
import app.mybad.notifier.ui.screens.settings.profile.SettingsProfileContract
import app.mybad.notifier.ui.screens.settings.profile.SettingsProfileScreen
import app.mybad.notifier.ui.screens.settings.profile.SettingsProfileViewModel
import app.mybad.notifier.ui.screens.settings.wishes.SettingsLeaveWishesContract
import app.mybad.notifier.ui.screens.settings.wishes.SettingsLeaveWishesScreen
import app.mybad.notifier.ui.screens.settings.wishes.SettingsLeaveWishesViewModel

fun NavGraphBuilder.settingsNavGraph(
    navigationState: NavigationState,
    toAuthorizationRequested: () -> Unit
) {
    navigation(
        startDestination = SettingsScreens.Navigation.route,
        route = MainScreens.Settings.route
    ) {
        composable(route = SettingsScreens.Navigation.route) {
            val viewModel: SettingsMainViewModel = hiltViewModel()
            SettingsMainScreen(
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationEffect ->
                    when (navigationEffect) {
                        SettingsMainScreenContract.Effect.Navigation.ToProfile -> {
                            navigationState.navigateSingleTo(SettingsScreens.Profile.route)
                        }

                        SettingsMainScreenContract.Effect.Navigation.ToSystemNotificationsSettings -> {
                            navigationState.navigateSingleTo(SettingsScreens.NotificationsSystem.route)
                        }

                        SettingsMainScreenContract.Effect.Navigation.ToAbout -> {
                            navigationState.navigateSingleTo(SettingsScreens.About.route)
                        }


                        SettingsMainScreenContract.Effect.Navigation.ToLeaveWishes -> {
                            navigationState.navigateSingleTo(SettingsScreens.LeaveYourWishes.route)
                        }
                    }
                }
            )
        }

        composable(route = SettingsScreens.Profile.route) {
            val viewModel: SettingsProfileViewModel = hiltViewModel()
            SettingsProfileScreen(
                state = viewModel.viewState.value,
                events = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationEffect ->
                    when (navigationEffect) {
                        SettingsProfileContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }

                        SettingsProfileContract.Effect.Navigation.ToChangePassword -> {
                            navigationState.navigateSingleTo(SettingsScreens.PasswordChange.route)
                        }

                        SettingsProfileContract.Effect.Navigation.ToAuthorization -> {
                            toAuthorizationRequested()
                        }
                    }
                }
            )
            BackHandler {
                viewModel.setEvent(SettingsProfileContract.Event.ActionBack)
            }
        }

        composable(route = SettingsScreens.NotificationsSystem.route) {
            val viewModel: SettingsNotificationsViewModel = hiltViewModel()
            SettingsNotificationsScreen(
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationEffect ->
                    when (navigationEffect) {
                        SettingsNotificationsContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }
                    }
                }
            )
        }

        composable(SettingsScreens.LeaveYourWishes.route) {
            val viewModel: SettingsLeaveWishesViewModel = hiltViewModel()
            SettingsLeaveWishesScreen(
                state = viewModel.viewState.value,
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationEffect ->
                    when (navigationEffect) {
                        SettingsLeaveWishesContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }
                    }
                }
            )

        }

        composable(SettingsScreens.About.route) {
            val viewModel: SettingsAboutViewModel = hiltViewModel()
            SettingsAboutScreen(
                state = viewModel.viewState.value,
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationEffect ->
                    when (navigationEffect) {
                        SettingsAboutContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }

                        SettingsAboutContract.Effect.Navigation.AboutOurTeam -> {
                            navigationState.navigateSingleTo(SettingsScreens.AboutOurTeam.route)
                        }
                    }
                }
            )
        }

        composable(SettingsScreens.AboutOurTeam.route) {
            val viewModel: SettingsAboutTeamViewModel = hiltViewModel()
            SettingsAboutTeamScreen(
                state = viewModel.viewState.value,
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationEffect ->
                    when (navigationEffect) {
                        SettingsAboutTeamContract.Effect.Navigation.Back -> navigationState.navController.popBackStack()
                    }
                }
            )
        }

        composable(SettingsScreens.PasswordChange.route) {
            val viewModel: SettingsChangePasswordViewModel = hiltViewModel()
            SettingsChangePasswordScreen(
                state = viewModel.viewState.value,
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationEffect ->
                    when (navigationEffect) {
                        SettingsChangePasswordContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }
                    }
                }
            )
        }
    }
}
