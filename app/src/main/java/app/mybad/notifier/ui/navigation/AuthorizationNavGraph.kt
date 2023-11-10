package app.mybad.notifier.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import app.mybad.notifier.ui.screens.authorization.login.LoginContract
import app.mybad.notifier.ui.screens.authorization.login.LoginViewModel
import app.mybad.notifier.ui.screens.authorization.login.MainLoginScreen
import app.mybad.notifier.ui.screens.authorization.newpassword.NewPasswordScreen
import app.mybad.notifier.ui.screens.authorization.newpassword.NewPasswordScreenContract
import app.mybad.notifier.ui.screens.authorization.newpassword.NewPasswordScreenViewModel
import app.mybad.notifier.ui.screens.authorization.passwords.PasswordRecoveryScreenContract
import app.mybad.notifier.ui.screens.authorization.passwords.PasswordRecoveryViewModel
import app.mybad.notifier.ui.screens.authorization.passwords.StartPasswordRecoveryScreen
import app.mybad.notifier.ui.screens.authorization.recoverycodeverification.RecoveryCodeVerificationScreen
import app.mybad.notifier.ui.screens.authorization.recoverycodeverification.RecoveryCodeVerificationScreenContract
import app.mybad.notifier.ui.screens.authorization.recoverycodeverification.RecoveryCodeVerificationViewModel
import app.mybad.notifier.ui.screens.authorization.registration.RegistrationContract
import app.mybad.notifier.ui.screens.authorization.registration.RegistrationViewModel
import app.mybad.notifier.ui.screens.authorization.registration.StartRegistrationScreen
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
                            navigationState.navigateSingleTo(AppScreens.NotificationRequest.route)
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
                        PasswordRecoveryScreenContract.Effect.Navigation.ToAuthorization -> {
                            navigationState.navController.popBackStack()
                        }

                        PasswordRecoveryScreenContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }

                        is PasswordRecoveryScreenContract.Effect.Navigation.ToCodeVerification -> {
                            navigationState.navController.popBackStack()
                            navigationState.navigateSingleTo(
                                route = AuthorizationScreens.RecoveryCodeVerification.getRouteWithArgs(
                                    navigationAction.email
                                )
                            )
                        }
                    }
                }
            )
        }

        composable(
            route = AuthorizationScreens.RecoveryCodeVerification.route,
            arguments = listOf(
                navArgument(AuthorizationScreens.RecoveryCodeVerification.EMAIL_ARG) {
                    type = NavType.StringType
                }
            )
        ) {
            val email =
                it.arguments?.getString(AuthorizationScreens.RecoveryCodeVerification.EMAIL_ARG)
                    ?: ""
            val viewModel: RecoveryCodeVerificationViewModel = hiltViewModel()
            viewModel.setEmail(email)
            RecoveryCodeVerificationScreen(
                state = viewModel.viewState.value,
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationAction ->
                    when (navigationAction) {
                        RecoveryCodeVerificationScreenContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }

                        is RecoveryCodeVerificationScreenContract.Effect.Navigation.ToNewPassword -> {
                            navigationState.navController.popBackStack()
                            navigationState.navigateSingleTo(
                                AuthorizationScreens.NewPassword.getRouteWithArgs(
                                    token = navigationAction.token,
                                    email = navigationAction.email
                                )
                            )
                        }
                    }
                }
            )
        }

        composable(AuthorizationScreens.NewPassword.route,
            arguments = listOf(
                navArgument(AuthorizationScreens.NewPassword.TOKEN_ARG) {
                    type = NavType.StringType
                },
                navArgument(AuthorizationScreens.NewPassword.EMAIL_ARG) {
                    type = NavType.StringType
                }
            )) {
            val token = it.arguments?.getString(AuthorizationScreens.NewPassword.TOKEN_ARG) ?: ""
            val email = it.arguments?.getString(AuthorizationScreens.NewPassword.EMAIL_ARG) ?: ""
            val viewModel: NewPasswordScreenViewModel = hiltViewModel()
            viewModel.setNavigationParams(token, email)
            NewPasswordScreen(
                state = viewModel.viewState.value,
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationAction ->
                    when (navigationAction) {
                        NewPasswordScreenContract.Effect.Navigation.ToAuthorization -> {
                            navigationState.navController.popBackStack()
                            navigationState.navigateSingleTo(AuthorizationScreens.Login.route)
                        }

                        NewPasswordScreenContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }
                    }
                }
            )
        }
    }
}
