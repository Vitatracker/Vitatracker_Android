package app.mybad.notifier.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    splashScreenContent: @Composable () -> Unit,
    authorizationChooseModeScreenContent: @Composable () -> Unit,
    authorizationLoginScreenContent: @Composable () -> Unit,
    authorizationRegistrationScreenContent: @Composable () -> Unit,
    mainScreenContent: @Composable () -> Unit
) {
    NavHost(navController = navHostController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            splashScreenContent()
        }
        authorizationNavGraph(
            authorizationChooseModeScreenContent = authorizationChooseModeScreenContent,
            authorizationLoginScreenContent = authorizationLoginScreenContent,
            authorizationRegistrationScreenContent = authorizationRegistrationScreenContent
        )
        composable(Screen.Main.route) {
            mainScreenContent()
        }
    }
}