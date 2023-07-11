package app.mybad.notifier.ui.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

fun NavGraphBuilder.authorizationNavGraph(
    authorizationChooseModeScreenContent: @Composable () -> Unit,
    authorizationLoginScreenContent: @Composable () -> Unit,
    authorizationRegistrationScreenContent: @Composable () -> Unit
) {
    navigation(startDestination = Screen.AuthorizationChooseMode.route, route = Screen.Authorization.route) {
        composable(route = Screen.AuthorizationChooseMode.route) {
            authorizationChooseModeScreenContent()
        }
        composable(route = Screen.AuthorizationLogin.route) {
            authorizationLoginScreenContent()
        }
        composable(route = Screen.AuthorizationRegistration.route) {
            authorizationRegistrationScreenContent()
        }
    }
}