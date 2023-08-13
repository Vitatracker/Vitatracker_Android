package app.mybad.notifier.ui.navigation

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.mybad.notifier.ui.screens.main.MainScreen
import app.mybad.notifier.ui.screens.mycourses.edit.MyCourseEditScreen
import app.mybad.notifier.ui.screens.mycourses.MyCoursesContract
import app.mybad.notifier.ui.screens.mycourses.MyCoursesViewModel
import app.mybad.notifier.ui.screens.splash.SplashScreenContract
import app.mybad.notifier.ui.screens.splash.SplashScreenViewModel
import app.mybad.notifier.ui.screens.splash.StartSplashScreen

@Composable
fun AppNavGraph() {
    val navigationState = rememberNavigationState()
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
            composable(
                route = AppScreens.EditCourse.routeWithArgs,
                arguments = AppScreens.EditCourse.arguments,
            ) { navBackStackEntry ->
                val viewModel: MyCoursesViewModel = hiltViewModel()
                val arguments = requireNotNull(navBackStackEntry.arguments)
                val courseId = arguments.getLong(AppScreens.EditCourse.courseIdArg)
                Log.w(
                    "VTTAG",
                    "AppNavGraph::AppScreens.EditCourse.route: in course=$courseId"
                )
                // загрузим в стейт
                viewModel.uploadCourseForEditingInState(courseId)

                MyCourseEditScreen(
                    state = viewModel.viewState.value,
                    effectFlow = viewModel.effect,
                    sendEvent = viewModel::setEvent,
                    navigation = { navigationAction ->
                        when (navigationAction) {
                            is MyCoursesContract.Effect.Navigation.ToCourseEditing -> {}

                            MyCoursesContract.Effect.Navigation.Back -> {
                                Log.w(
                                    "VTTAG",
                                    "AppNavGraph::CourseInfoScreen: navigate->popBackStack"
                                )
                                navigationState.navController.popBackStack()
                            }
                        }
                    }
                )
            }
        }
    }

}
