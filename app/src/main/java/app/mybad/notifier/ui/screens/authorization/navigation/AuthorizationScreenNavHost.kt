package app.mybad.notifier.ui.screens.authorization.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.mybad.notifier.ui.screens.authorization.AuthorizationScreenViewModel
import app.mybad.notifier.ui.screens.authorization.StartAuthorizationScreen
import app.mybad.notifier.ui.screens.authorization.login.StartMainLoginScreen
import app.mybad.notifier.ui.screens.authorization.passwords.StartMainNewPasswordScreenAuth
import app.mybad.notifier.ui.screens.authorization.passwords.StartMainRecoveryPasswordScreenAuth
import app.mybad.notifier.ui.screens.authorization.registration.StartMainRegistrationScreen
import app.mybad.notifier.ui.screens.start.StartScreenApp

@Composable
fun AuthorizationScreenNavHost(authVM: AuthorizationScreenViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthorizationNavItem.Authorization.route
    ) {
        composable(route = AuthorizationNavItem.Welcome.route) {
            StartScreenApp(navController = navController)
        }
        composable(route = AuthorizationNavItem.Authorization.route) {
            StartAuthorizationScreen(navController = navController, authVM = authVM)
        }
        composable(route = AuthorizationNavItem.Login.route) {
            StartMainLoginScreen(navController = navController, authVM = authVM)
        }
        composable(route = AuthorizationNavItem.Registration.route) {
            StartMainRegistrationScreen(navController = navController, authVM = authVM)
        }
        composable(route = AuthorizationNavItem.RecoveryPassword.route) {
            StartMainRecoveryPasswordScreenAuth(navController = navController)
        }
        composable(route = AuthorizationNavItem.NewPassword.route) {
            StartMainNewPasswordScreenAuth(navController = navController)
        }
    }
}
