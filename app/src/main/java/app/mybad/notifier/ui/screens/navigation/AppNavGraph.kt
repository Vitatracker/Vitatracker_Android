package app.mybad.notifier.ui.screens.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.mybad.notifier.ui.screens.splash.SplashScreen

@Composable
fun AppNavGraph(navigationState: NavigationState) {
    NavHost(navController = navigationState.navController, startDestination = Screen.Splash.route, enterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(500)
        )
    }) {
        composable(Screen.Splash.route) {
            SplashScreen(
                proceedToMain = {
                    navigationState.navigateToMain()
                },
                proceedToAuthorization = {
                    navigationState.navigateToAuthorization()
                }
            )
        }
        authorizationNavGraph(navigationState)
        composable(Screen.Main.route) {
            Text(text = "MainScreen")
        }
    }
}