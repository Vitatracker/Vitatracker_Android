package app.mybad.notifier.ui.screens.authorization.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.mybad.notifier.ui.screens.authorization.StartAuthorizationScreen
import app.mybad.notifier.ui.screens.authorization.login.StartMainLoginScreen
import app.mybad.notifier.ui.screens.authorization.passwords.StartMainNewPasswordScreenAuth
import app.mybad.notifier.ui.screens.authorization.passwords.StartMainRecoveryPasswordScreenAuth
import app.mybad.notifier.ui.screens.authorization.registration.StartMainRegistrationScreen
import app.mybad.notifier.ui.screens.start.StartScreenApp

@Composable
fun AuthorizationScreenNavHost() {

    val navController = rememberNavController()

    Column(modifier = Modifier) {
        NavHost(
            navController = navController,
            startDestination = AuthorizationNavItem.Authorization.route
        ) {
            composable(route = AuthorizationNavItem.Welcome.route) {
                StartScreenApp(navController = navController)
            }
            composable(route = AuthorizationNavItem.Authorization.route) {
                StartAuthorizationScreen(navController = navController)
            }
            composable(route = AuthorizationNavItem.Login.route) {
                StartMainLoginScreen(navController = navController)
            }
            composable(route = AuthorizationNavItem.Registration.route) {
                StartMainRegistrationScreen(navController = navController)
            }
            composable(route = AuthorizationNavItem.RecoveryPassword.route) {
                StartMainRecoveryPasswordScreenAuth(navController = navController)
            }
            composable(route = AuthorizationNavItem.NewPassword.route) {
                StartMainNewPasswordScreenAuth(navController = navController)
            }
        }
    }

}