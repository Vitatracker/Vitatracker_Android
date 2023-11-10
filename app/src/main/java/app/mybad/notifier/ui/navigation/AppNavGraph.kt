package app.mybad.notifier.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.mybad.notifier.ui.screens.main.MainScreen
import app.mybad.notifier.ui.screens.splash.SplashScreenContract
import app.mybad.notifier.ui.screens.splash.SplashScreenViewModel
import app.mybad.notifier.ui.screens.splash.StartSplashScreen

@Composable
fun AppNavGraph() {
    val navigationState = rememberNavigationState()
    var visible by remember { mutableStateOf(true) }

    Scaffold { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navigationState.navController,
            startDestination = AppScreens.Splash.route,
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
        }
    }
}
