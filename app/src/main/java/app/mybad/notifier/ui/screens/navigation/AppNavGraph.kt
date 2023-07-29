package app.mybad.notifier.ui.screens.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.mybad.notifier.ui.screens.mainscreen.MainScreen
import app.mybad.notifier.ui.screens.splash.SplashScreen
import app.mybad.notifier.ui.screens.splash.SplashScreenContract
import app.mybad.notifier.ui.screens.splash.SplashScreenViewModel

@Composable
fun AppNavGraph() {
    val navigationState = rememberNavigationState()
    Scaffold { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navigationState.navController,
            startDestination = Screen.Splash.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }
        ) {
            composable(Screen.Splash.route) {
                val viewModel: SplashScreenViewModel = hiltViewModel()
                SplashScreen(
                    state = viewModel.viewState.value,
                    events = viewModel.effect,
                    onEventSent = { viewModel.setEvent(it) },
                    onNavigationRequested = { navigationAction ->
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
            composable(Screen.Main.route) {
                MainScreen(
                    onAddClicked = {
                        navigationState.navigateSingleTo(Screen.AddCourse.route)
                    }
                )
            }
            addCourseNavGraph(navigationState)
            profileNavGraph(
                navigationState = navigationState,
                onExitToAuthorization = {
                    navigationState.navController.popBackStack(Screen.Profile.route, true)
                    navigationState.navigateToAuthorization()
                }
            )
        }
    }

}