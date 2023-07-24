package app.mybad.notifier.ui.screens.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import app.mybad.notifier.ui.screens.authorization.StartAuthorizationScreen
import app.mybad.notifier.ui.screens.authorization.login.LoginScreenContract
import app.mybad.notifier.ui.screens.authorization.login.LoginScreenViewModel
import app.mybad.notifier.ui.screens.authorization.login.StartMainLoginScreen
import app.mybad.notifier.ui.screens.authorization.passwords.RecoverPasswordScreenContract
import app.mybad.notifier.ui.screens.authorization.passwords.RecoverPasswordScreenViewModel
import app.mybad.notifier.ui.screens.authorization.passwords.StartMainNewPasswordScreenAuth
import app.mybad.notifier.ui.screens.authorization.passwords.StartMainRecoveryPasswordScreenAuth
import app.mybad.notifier.ui.screens.authorization.registration.RegistrationScreenContract
import app.mybad.notifier.ui.screens.authorization.registration.RegistrationScreenViewModel
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
            val viewModel: LoginScreenViewModel = hiltViewModel()
            StartMainLoginScreen(
                state = viewModel.viewState.value,
                events = viewModel.effect,
                onEventSent = { viewModel.setEvent(it) },
                onNavigationRequested = { navigationAction ->
                    when (navigationAction) {
                        LoginScreenContract.Effect.Navigation.ToForgotPassword -> {
                            navigationState.navigateSingleTo(AuthorizationScreens.PasswordRecovery.route)
                        }

                        LoginScreenContract.Effect.Navigation.ToMain -> {
                            navigationState.navController.popBackStack(AuthorizationScreens.ChooseMode.route, true)
                            navigationState.navigateToMain()
                        }

                        LoginScreenContract.Effect.Navigation.Back -> navigationState.navController.popBackStack()
                    }
                }
            )
        }
        composable(route = AuthorizationScreens.Registration.route) {
            val viewModel: RegistrationScreenViewModel = hiltViewModel()
            StartMainRegistrationScreen(
                state = viewModel.viewState.value,
                events = viewModel.effect,
                onEventSent = { viewModel.setEvent(it) },
                onNavigationRequested = { navigationAction ->
                    when (navigationAction) {
                        RegistrationScreenContract.Effect.Navigation.Back -> navigationState.navController.popBackStack()
                        RegistrationScreenContract.Effect.Navigation.ToMain -> {
                            navigationState.navController.popBackStack(AuthorizationScreens.ChooseMode.route, true)
                            navigationState.navigateToMain()
                        }
                    }
                }
            )
        }
        composable(AuthorizationScreens.PasswordRecovery.route) {
            val viewModel: RecoverPasswordScreenViewModel = hiltViewModel()
            StartMainRecoveryPasswordScreenAuth(
                state = viewModel.viewState.value,
                events = viewModel.effect,
                onEventSent = { viewModel.setEvent(it) },
                onNavigationRequested = { navigationAction ->
                    when (navigationAction) {
                        RecoverPasswordScreenContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }

                        RecoverPasswordScreenContract.Effect.Navigation.ToAuthorization -> {
                            navigationState.navController.popBackStack()
                        }
                    }
                }
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