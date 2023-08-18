package app.mybad.notifier.ui.screens.navigation

import androidx.activity.compose.BackHandler
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import app.mybad.notifier.ui.screens.newcourse.CreateCourseScreensContract
import app.mybad.notifier.ui.screens.newcourse.CreateCourseViewModel
import app.mybad.notifier.ui.screens.newcourse.screens.AddCourseMainScreen
import app.mybad.notifier.ui.screens.newcourse.screens.AddMedicineFirstScreen
import app.mybad.notifier.ui.screens.newcourse.screens.AddMedicineSecondScreen
import app.mybad.notifier.ui.screens.newcourse.screens.SuccessMainScreen

fun NavGraphBuilder.addCourseNavGraph(navigationState: NavigationState) {
    navigation(startDestination = AddCourseScreens.MedDetailsScreen.route, route = Screen.AddCourse.route) {
        composable(route = AddCourseScreens.MedDetailsScreen.route) { entry ->
            val viewModel = entry.sharedViewModel<CreateCourseViewModel>(navigationState.navController)
            AddMedicineFirstScreen(
                state = viewModel.viewState.value,
                events = viewModel.effect,
                onEventSent = { viewModel.setEvent(it) },
                onNavigationRequested = { navigationAction ->
                    when (navigationAction) {
                        CreateCourseScreensContract.Effect.Navigation.ActionBack -> navigationState.navController.popBackStack()
                        CreateCourseScreensContract.Effect.Navigation.ActionNext -> {
                            navigationState.navigateSingleTo(AddCourseScreens.MedReceptionScreen.route)
                        }
                    }
                }
            )
        }
        composable(route = AddCourseScreens.MedReceptionScreen.route) { entry ->
            val viewModel = entry.sharedViewModel<CreateCourseViewModel>(navigationState.navController)
            AddMedicineSecondScreen(
                state = viewModel.viewState.value,
                events = viewModel.effect,
                onEventSent = { viewModel.setEvent(it) },
                onNavigationRequested = { navigationAction ->
                    when (navigationAction) {
                        CreateCourseScreensContract.Effect.Navigation.ActionBack -> navigationState.navController.popBackStack()
                        CreateCourseScreensContract.Effect.Navigation.ActionNext -> {
                            navigationState.navigateSingleTo(AddCourseScreens.CourseDetailsScreen.route)
                        }
                    }
                }
            )
        }
        composable(route = AddCourseScreens.CourseDetailsScreen.route) { entry ->
            val viewModel = entry.sharedViewModel<CreateCourseViewModel>(navigationState.navController)
            AddCourseMainScreen(
                state = viewModel.viewState.value,
                events = viewModel.effect,
                onEventSent = { viewModel.setEvent(it) },
                onNavigationRequested = { navigationAction ->
                    when (navigationAction) {
                        CreateCourseScreensContract.Effect.Navigation.ActionBack -> navigationState.navController.popBackStack()
                        CreateCourseScreensContract.Effect.Navigation.ActionNext -> {
                            navigationState.navigateSingleTo(AddCourseScreens.CourseDurationScreen.route)
                        }
                    }
                }
            )
        }
        composable(route = AddCourseScreens.CourseDurationScreen.route) { entry ->
            val viewModel = entry.sharedViewModel<CreateCourseViewModel>(navigationState.navController)
//            val state by viewModel.state.collectAsStateWithLifecycle()
//            AddNotificationsMainScreen(
//                med = state.med,
//                onNext = {
//                    viewModel.reduce(NewCourseIntent.Finish)
//                    navigationState.navController.navigate(AddCourseScreens.CongratulationsScreen.route) {
//                        launchSingleTop = true
//                        popUpTo(AddCourseScreens.MedDetailsScreen.route) {
//                            inclusive = true
//                        }
//                    }
//                },
//                onBackPressed = {
//                    navigationState.navController.popBackStack()
//                }
//            )
        }
        composable(route = AddCourseScreens.CongratulationsScreen.route) {
            SuccessMainScreen {
                navigationState.navController.popBackStack()
            }
            BackHandler {
                // back press not allowed
            }
        }
    }
}