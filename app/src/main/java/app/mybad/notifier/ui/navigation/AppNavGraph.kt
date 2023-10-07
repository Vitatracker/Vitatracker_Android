package app.mybad.notifier.ui.navigation

import android.content.Intent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import app.mybad.notifier.ui.screens.main.MainScreen
import app.mybad.notifier.ui.screens.newpassword.NewPasswordScreenContract
import app.mybad.notifier.ui.screens.newpassword.NewPasswordScreenViewModel
import app.mybad.notifier.ui.screens.newpassword.StartNewPasswordScreen
import app.mybad.notifier.ui.screens.splash.SplashScreenContract
import app.mybad.notifier.ui.screens.splash.SplashScreenViewModel
import app.mybad.notifier.ui.screens.splash.StartSplashScreen

@Composable
fun AppNavGraph(navigationState: NavigationState) {

    Scaffold { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navigationState.navController,
            startDestination = AppScreens.Splash.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }
        ) {
            composable(route = AppScreens.Splash.route) {
                val viewModel: SplashScreenViewModel = hiltViewModel()

                StartSplashScreen(
                    state = viewModel.viewState.value,
                    effectFlow = viewModel.effect,
                    sendEvent = viewModel::setEvent,
                    navigation = { navigationAction ->
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
            composable(route = AppScreens.Main.route) {
                MainScreen(
                    navigateUp = { route ->
                        when (route) {
                            AppScreens.Authorization.route -> navigationState.navigateToAuthorization()
                            else -> navigationState.navigateSingleTo(route)
                        }
                    }
                )
            }
            //route = AppScreens.AddCourse.route,
            newCourseNavGraph(navigationState)
            //route = AppScreens.EditCourse.route,
            editCourseNavGraph(navigationState)

            composable(
                route = AppScreens.NewPassword.route,
                deepLinks = listOf(navDeepLink {
                    uriPattern = "https://vitatracker-heroku.herokuapp.com/api/v1/auth/password/reset/{argument}"
                    action = Intent.ACTION_VIEW
                }),
                arguments = listOf(
                    navArgument("argument") {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
            ) { entry ->
                val refreshToken = entry.arguments?.getString("argument")
                val viewModel: NewPasswordScreenViewModel = hiltViewModel()
//                viewModel.updateUsersRefreshToken(refreshToken)
                StartNewPasswordScreen(
                    viewModel.viewState.value,
                    viewModel.effect,
                    viewModel::setEvent,
                    navigation = { navigationAction ->
                        when (navigationAction) {
                            NewPasswordScreenContract.Effect.Navigation.ToAuthorization -> {
                                navigationState.navigateToAuthorization()
                            }
                        }
                    }
                )
            }
        }
    }
}
