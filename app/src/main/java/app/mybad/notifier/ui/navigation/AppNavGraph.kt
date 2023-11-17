package app.mybad.notifier.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.mybad.notifier.ui.screens.main.MainScreen
import app.mybad.notifier.ui.screens.settings.notifications.SettingsNotificationsContract
import app.mybad.notifier.ui.screens.settings.notifications.SettingsNotificationsScreen
import app.mybad.notifier.ui.screens.settings.notifications.SettingsNotificationsViewModel
import app.mybad.notifier.ui.screens.settings.notifications_request.NotificationRequestContract
import app.mybad.notifier.ui.screens.settings.notifications_request.NotificationRequestScreen
import app.mybad.notifier.ui.screens.settings.notifications_request.NotificationRequestViewModel
import app.mybad.notifier.ui.screens.splash.SplashScreenContract
import app.mybad.notifier.ui.screens.splash.SplashScreenViewModel
import app.mybad.notifier.ui.screens.splash.StartSplashScreen

@Composable
fun AppNavGraph() {
    val navigationState = rememberNavigationState()
    var toMainScreen by rememberSaveable { mutableStateOf(false) }

    Scaffold { paddingValues ->
        NavHost(
            modifier = Modifier.padding(
                start = 0.dp,
                end = 0.dp,
                top = paddingValues.calculateTopPadding(),
                bottom = 0.dp
            ),
            navController = navigationState.navController,
            startDestination = AppScreens.Splash.route,
        ) {
            composable(route = AppScreens.Splash.route) {
                val viewModel: SplashScreenViewModel = hiltViewModel()

                StartSplashScreen(
                    state = viewModel.viewState.value,
                    effectFlow = viewModel.effect,
                    sendEvent = viewModel::setEvent,
                    navigation = { navigationAction ->
                        when (navigationAction) {
                            SplashScreenContract.Effect.Navigation.ToAuthorization -> {
                                navigationState.navigateToAuthorization()
                            }

                            SplashScreenContract.Effect.Navigation.ToMain -> {
                                navigationState.navigateToMain()
                            }
                        }
                    }
                )
            }
            authorizationNavGraph(navigationState)
            composable(route = AppScreens.NotificationRequest.route) {
                val viewModel: NotificationRequestViewModel = hiltViewModel()
                NotificationRequestScreen(
                    state = viewModel.viewState.value,
                    effectFlow = viewModel.effect,
                    sendEvent = viewModel::setEvent,
                    navigation = { navigationAction ->
                        when (navigationAction) {
                            NotificationRequestContract.Effect.Navigation.ToNext -> {
                                navigationState.navigateToMain()
                            }

                            NotificationRequestContract.Effect.Navigation.ToSettings -> {
                                toMainScreen = true
                                navigationState.navController.popBackStack()
                                navigationState.navigateSingleTo(
                                    route = AppScreens.NotificationsSettings.route
                                )
                            }
                        }
                    }
                )
            }
            composable(route = AppScreens.NotificationsSettings.route) {
                val viewModel: SettingsNotificationsViewModel = hiltViewModel()
                SettingsNotificationsScreen(
                    effectFlow = viewModel.effect,
                    sendEvent = viewModel::setEvent,
                    navigation = { navigationEffect ->
                        when (navigationEffect) {
                            SettingsNotificationsContract.Effect.Navigation.Back -> {
                                if (toMainScreen) {
                                    toMainScreen = false
                                    navigationState.navigateToMain()
                                } else navigationState.navController.popBackStack()
                            }
                        }
                    }
                )
            }
            composable(route = AppScreens.Main.route) {
                MainScreen { route ->
                    when (route) {
                        AppScreens.Authorization.route -> navigationState.navigateToAuthorization()
                        else -> navigationState.navigateSingleTo(route)
                    }
                }
            }
            //route = AppScreens.AddCourse.route,
            newCourseNavGraph(navigationState)
            //route = AppScreens.EditCourse.route,
            editCourseNavGraph(navigationState)
        }
    }
}
