package app.mybad.notifier.ui.screens.navigation

import androidx.activity.compose.BackHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import app.mybad.notifier.ui.screens.settings.about.SettingsAbout
import app.mybad.notifier.ui.screens.settings.about.SettingsAboutScreenContract
import app.mybad.notifier.ui.screens.settings.about.SettingsAboutViewModel
import app.mybad.notifier.ui.screens.settings.about_team.SettingsAboutOurTeamScreen
import app.mybad.notifier.ui.screens.settings.about_team.SettingsAboutOurTeamScreenContract
import app.mybad.notifier.ui.screens.settings.about_team.SettingsAboutOurTeamScreenViewModel
import app.mybad.notifier.ui.screens.settings.main.SettingsMainScreenContract
import app.mybad.notifier.ui.screens.settings.main.SettingsMainViewModel
import app.mybad.notifier.ui.screens.settings.main.SettingsNavScreen
import app.mybad.notifier.ui.screens.settings.profile.ProfileScreenContract
import app.mybad.notifier.ui.screens.settings.profile.ProfileScreenViewModel
import app.mybad.notifier.ui.screens.settings.profile.SettingsProfileScreen
import app.mybad.notifier.ui.screens.settings.wishes.SettingsLeaveWishes
import app.mybad.notifier.ui.screens.settings.wishes.SettingsLeaveWishesScreenContract
import app.mybad.notifier.ui.screens.settings.wishes.SettingsLeaveWishesScreenViewModel

fun NavGraphBuilder.settingsNavGraph(navigationState: NavigationState, toAuthorizationRequested: () -> Unit) {
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

                        SettingsMainScreenContract.Effect.Navigation.ToNotificationsSettings -> {
                            navigationState.navigateSingleTo(SettingsScreens.Notifications.route)
                        }

                        SettingsMainScreenContract.Effect.Navigation.ToAbout -> {
                            navigationState.navigateSingleTo(SettingsScreens.About.route)
                        }

                        SettingsMainScreenContract.Effect.Navigation.ToAvatarEdit -> {
//                            navigationState.navigateSingleTo(SettingsScreens..route)
                        }

                        SettingsMainScreenContract.Effect.Navigation.ToLeaveWishes -> {
                            navigationState.navigateSingleTo(SettingsScreens.LeaveYourWishes.route)
                        }
                    }
                }
            )
        }
        composable(route = SettingsScreens.Profile.route) {
            val viewModel: ProfileScreenViewModel = hiltViewModel()
            SettingsProfileScreen(
                state = viewModel.viewState.value,
                events = viewModel.effect,
                onEventSent = { viewModel.setEvent(it) },
                onNavigationRequested = { navigationEffect ->
                    when (navigationEffect) {
                        ProfileScreenContract.Effect.Navigation.Back -> navigationState.navController.popBackStack()
                        ProfileScreenContract.Effect.Navigation.ToChangePassword -> {}
                        ProfileScreenContract.Effect.Navigation.ToAuthorization -> {
                            toAuthorizationRequested()
                        }
                    }
                }
            )
            BackHandler {
                viewModel.setEvent(ProfileScreenContract.Event.ActionBack)
            }
        }
        composable(route = SettingsScreens.Notifications.route) {

        }
        composable(SettingsScreens.LeaveYourWishes.route) {
            val viewModel: SettingsLeaveWishesScreenViewModel = hiltViewModel()
            SettingsLeaveWishes(
                state = viewModel.viewState.value,
                events = viewModel.effect,
                onEventSent = { viewModel.setEvent(it) },
                onNavigationRequested = { navigationEffect ->
                    when (navigationEffect) {
                        SettingsLeaveWishesScreenContract.Effect.Navigation.Back -> navigationState.navController.popBackStack()
                    }
                }
            )

        }
        composable(SettingsScreens.About.route) {
            val viewModel: SettingsAboutViewModel = hiltViewModel()
            SettingsAbout(
                state = viewModel.viewState.value,
                events = viewModel.effect,
                onEventSent = { viewModel.setEvent(it) },
                onNavigationRequested = { navigationEffect ->
                    when (navigationEffect) {
                        SettingsAboutScreenContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }

                        SettingsAboutScreenContract.Effect.Navigation.AboutOurTeam -> {
                            navigationState.navigateSingleTo(SettingsScreens.AboutOurTeam.route)
                        }
                    }
                }
            )
        }

        composable(SettingsScreens.AboutOurTeam.route) {
            val viewModel: SettingsAboutOurTeamScreenViewModel = hiltViewModel()
            SettingsAboutOurTeamScreen(
                state = viewModel.viewState.value,
                events = viewModel.effect,
                onEventSent = { viewModel.setEvent(it) },
                onNavigationRequested = { navigationEffect ->
                    when (navigationEffect) {
                        SettingsAboutOurTeamScreenContract.Effect.Navigation.Back -> navigationState.navController.popBackStack()
                    }
                }
            )
        }
    }
}
