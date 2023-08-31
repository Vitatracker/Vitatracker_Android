package app.mybad.notifier.ui.navigation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import app.mybad.notifier.ui.screens.newcourse.CreateCourseContract
import app.mybad.notifier.ui.screens.newcourse.CreateCourseViewModel
import app.mybad.notifier.ui.screens.newcourse.screens.AddMedNotificationsScreen
import app.mybad.notifier.ui.screens.newcourse.screens.AddMedCourseDetailsScreen
import app.mybad.notifier.ui.screens.newcourse.screens.AddMedFirstScreen
import app.mybad.notifier.ui.screens.newcourse.screens.AddMedSecondScreen
import app.mybad.notifier.ui.screens.newcourse.screens.AddMedSuccessScreen

fun NavGraphBuilder.newCourseNavGraph(navigationState: NavigationState) {
    navigation(
        route = AppScreens.AddCourse.route,
        startDestination = NewCourseScreens.MedDetailsScreen.route,
    ) {
        composable(route = NewCourseScreens.MedDetailsScreen.route) { navBackStackEntry ->
            val viewModel = navBackStackEntry
                .sharedViewModel<CreateCourseViewModel>(navigationState.navController)

            AddMedFirstScreen(
                state = viewModel.viewState.value,
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationEffect ->
                    when (navigationEffect) {
                        CreateCourseContract.Effect.Navigation.Next -> {
                            navigationState.navigateSingleTo(NewCourseScreens.MedReceptionScreen.route)
                        }

                        CreateCourseContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }
                    }
                }
            )
        }
        composable(route = NewCourseScreens.MedReceptionScreen.route) { navBackStackEntry ->
            val viewModel =
                navBackStackEntry.sharedViewModel<CreateCourseViewModel>(navigationState.navController)

            AddMedSecondScreen(
                state = viewModel.viewState.value,
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationEffect ->
                    when (navigationEffect) {
                        CreateCourseContract.Effect.Navigation.Next -> {
                            Log.w(
                                "VTTAG",
                                "NewCourseNavGraph::navigateTo: route=${NewCourseScreens.CourseDetailsScreen.route}"
                            )
                            navigationState.navigateSingleTo(NewCourseScreens.CourseDetailsScreen.route)
                        }

                        CreateCourseContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }
                    }
                }
            )
        }
        composable(route = NewCourseScreens.CourseDetailsScreen.route) { navBackStackEntry ->
            val viewModel =
                navBackStackEntry.sharedViewModel<CreateCourseViewModel>(navigationState.navController)

            AddMedCourseDetailsScreen(
                state = viewModel.viewState.value,
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationEffect ->
                    when (navigationEffect) {
                        CreateCourseContract.Effect.Navigation.Next -> {
                            Log.w(
                                "VTTAG",
                                "NewCourseNavGraph::navigateTo: route=${NewCourseScreens.CourseDurationScreen.route}"
                            )
                            navigationState.navigateSingleTo(NewCourseScreens.CourseDurationScreen.route)
                        }

                        CreateCourseContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }
                    }
                }
            )
        }
        composable(route = NewCourseScreens.CourseDurationScreen.route) { navBackStackEntry ->
            val viewModel =
                navBackStackEntry.sharedViewModel<CreateCourseViewModel>(navigationState.navController)

            AddMedNotificationsScreen(
                state = viewModel.viewState.value,
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationEffect ->
                    when (navigationEffect) {
                        CreateCourseContract.Effect.Navigation.Next -> {
                            Log.w(
                                "VTTAG",
                                "NewCourseNavGraph::navigateTo: route=${NewCourseScreens.CongratulationsScreen.route}"
                            )
                            navigationState.navController.navigate(NewCourseScreens.CongratulationsScreen.route) {
                                launchSingleTop = true
                                popUpTo(NewCourseScreens.MedDetailsScreen.route) {
                                    inclusive = true
                                }
                            }
                        }

                        CreateCourseContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }
                    }
                }
            )
        }
        composable(route = NewCourseScreens.CongratulationsScreen.route) {
            AddMedSuccessScreen {
                Log.w("VTTAG", "NewCourseNavGraph::navigateTo: route=popBackStack")
                navigationState.navController.popBackStack()
            }
            BackHandler {
                // back press not allowed because of status bar color changing issue
            }
        }
    }
}
