package app.mybad.notifier.ui.screens.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import app.mybad.notifier.ui.screens.authorization.StartAuthorizationScreen
import app.mybad.notifier.ui.screens.authorization.login.StartMainLoginScreen
import app.mybad.notifier.ui.screens.authorization.passwords.StartMainNewPasswordScreenAuth
import app.mybad.notifier.ui.screens.authorization.passwords.StartMainRecoveryPasswordScreenAuth
import app.mybad.notifier.ui.screens.authorization.registration.StartMainRegistrationScreen

fun NavGraphBuilder.authorizationNavGraph(navigationState: NavigationState) {
    navigation(startDestination = AuthorizationScreens.ChooseMode.route, route = Screen.Authorization.route) {
        composable(route = AuthorizationScreens.ChooseMode.route) {
            StartAuthorizationScreen(
                onLoginButtonClicked = {
                    navigationState.navigateSingleTo(AuthorizationScreens.Login.route)
                },
                onRegistrationButtonClicked = {
                    navigationState.navigateSingleTo(AuthorizationScreens.Registration.route)
                },
                onSignInWithGoogleClicked = {
                    // TODO("Добавить вход через Google")
                }
            )
        }
        composable(route = AuthorizationScreens.Login.route) {
            StartMainLoginScreen(
                onBackPressed = { navigationState.navController.popBackStack() },
                onForgotPasswordClicked = { navigationState.navigateSingleTo(AuthorizationScreens.PasswordRecovery.route) },
                onLoginSuccess = {
                    navigationState.navController.popBackStack(AuthorizationScreens.ChooseMode.route, true)
                    navigationState.navigateToMain()
                }
            )
        }
        composable(route = AuthorizationScreens.Registration.route) {
            StartMainRegistrationScreen(
                onBackPressed = {
                    navigationState.navController.popBackStack()
                },
                onRegistrationSuccess = {
                    navigationState.navController.popBackStack(AuthorizationScreens.ChooseMode.route, true)
                    navigationState.navigateToMain()
                }
            )
        }
        composable(AuthorizationScreens.PasswordRecovery.route) {
            StartMainRecoveryPasswordScreenAuth(
                onBackPressed = {
                    navigationState.navController.popBackStack()
                },
                onContinueClicked = {}
            )
        }
        composable(AuthorizationScreens.NewPassword.route) {
            StartMainNewPasswordScreenAuth(
                onBackPressed = {
                    navigationState.navController.popBackStack()
                }
            )
        }
    }
}