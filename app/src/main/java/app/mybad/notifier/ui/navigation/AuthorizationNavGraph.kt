package app.mybad.notifier.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import app.mybad.notifier.ui.screens.authorization.login.LoginContract
import app.mybad.notifier.ui.screens.authorization.login.LoginViewModel
import app.mybad.notifier.ui.screens.authorization.login.MainLoginScreen
import app.mybad.notifier.ui.screens.authorization.passwords.StartNewPasswordScreen
import app.mybad.notifier.ui.screens.authorization.passwords.StartPasswordRecoveryScreen
import app.mybad.notifier.ui.screens.authorization.passwords.PasswordRecoveryContract
import app.mybad.notifier.ui.screens.authorization.passwords.PasswordRecoveryViewModel
import app.mybad.notifier.ui.screens.authorization.registration.StartRegistrationScreen
import app.mybad.notifier.ui.screens.authorization.registration.RegistrationContract
import app.mybad.notifier.ui.screens.authorization.registration.RegistrationViewModel
import app.mybad.notifier.ui.screens.authorization.start.AuthorizationContract
import app.mybad.notifier.ui.screens.authorization.start.AuthorizationViewModel
import app.mybad.notifier.ui.screens.authorization.start.StartAuthorizationScreen

fun NavGraphBuilder.authorizationNavGraph(navigationState: NavigationState) {
    navigation(
        startDestination = AuthorizationScreens.ChooseMode.route,
        route = AppScreens.Authorization.route
    ) {
        composable(route = AuthorizationScreens.ChooseMode.route) {
            val viewModel: AuthorizationViewModel = hiltViewModel()
            StartAuthorizationScreen(
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationAction ->
                    when (navigationAction) {
                        AuthorizationContract.Effect.Navigation.ToAuthorization -> {
                            navigationState.navigateSingleTo(AuthorizationScreens.Login.route)
                        }

                        AuthorizationContract.Effect.Navigation.ToRegistration -> {
                            navigationState.navigateSingleTo(AuthorizationScreens.Registration.route)
                        }
                    }
                }
            )
        }

        composable(route = AuthorizationScreens.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            MainLoginScreen(
                state = viewModel.viewState.value,
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationAction ->
                    when (navigationAction) {
                        LoginContract.Effect.Navigation.ToForgotPassword -> {
                            navigationState.navigateSingleTo(AuthorizationScreens.PasswordRecovery.route)
                        }

                        LoginContract.Effect.Navigation.ToMain -> {
                            navigationState.navController.popBackStack(
                                AuthorizationScreens.ChooseMode.route,
                                true
                            )
                            navigationState.navigateToMain()
                        }

                        LoginContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }
                    }
                }
            )
        }

        composable(route = AuthorizationScreens.Registration.route) {
            val viewModel: RegistrationViewModel = hiltViewModel()
            StartRegistrationScreen(
                state = viewModel.viewState.value,
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationAction ->
                    when (navigationAction) {
                        RegistrationContract.Effect.Navigation.ToMain -> {
                            navigationState.navController.popBackStack(
                                AuthorizationScreens.ChooseMode.route,
                                true
                            )
                            navigationState.navigateToMain()
                        }

                        RegistrationContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }
                    }
                }
            )
        }

        composable(AuthorizationScreens.PasswordRecovery.route) {
            val viewModel: PasswordRecoveryViewModel = hiltViewModel()
            StartPasswordRecoveryScreen(
                state = viewModel.viewState.value,
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationAction ->
                    when (navigationAction) {
                        PasswordRecoveryContract.Effect.Navigation.ToAuthorization -> {
                            navigationState.navController.popBackStack()
                        }

                        PasswordRecoveryContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }
                    }
                }
            )
        }

        composable(AuthorizationScreens.NewPassword.route) {
            StartNewPasswordScreen(
                onBackPressed = {
                    navigationState.navController.popBackStack()
                }
            )
        }
    }
}
