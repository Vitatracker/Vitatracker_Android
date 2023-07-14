package app.mybad.notifier.ui.screens.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import app.mybad.notifier.ui.screens.newcourse.CreateCourseViewModel
import app.mybad.notifier.ui.screens.newcourse.NewCourseIntent
import app.mybad.notifier.ui.screens.newcourse.screens.AddCourseMainScreen
import app.mybad.notifier.ui.screens.newcourse.screens.AddMedicineFirstScreen
import app.mybad.notifier.ui.screens.newcourse.screens.AddMedicineSecondScreen
import app.mybad.notifier.ui.screens.newcourse.screens.AddNotificationsMainScreen
import app.mybad.notifier.ui.screens.newcourse.screens.SuccessMainScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

fun NavGraphBuilder.addCourseNavGraph(navigationState: NavigationState) {
    navigation(startDestination = AddCourseScreens.MedDetailsScreen.route, route = Screen.AddCourse.route) {
        composable(route = AddCourseScreens.MedDetailsScreen.route) { entry ->
            val viewModel = entry.sharedViewModel<CreateCourseViewModel>(navigationState.navController)
            val state by viewModel.state.collectAsStateWithLifecycle()
            AddMedicineFirstScreen(
                med = state.med,
                onNext = {
                    viewModel.reduce(NewCourseIntent.UpdateMed(it))
                    navigationState.navigateSingleTo(AddCourseScreens.MedReceptionScreen.route)
                },
                onBackPressed = {
                    navigationState.navController.popBackStack()
                }
            )
        }
        composable(route = AddCourseScreens.MedReceptionScreen.route) { entry ->
            val viewModel = entry.sharedViewModel<CreateCourseViewModel>(navigationState.navController)
            val state by viewModel.state.collectAsStateWithLifecycle()
            AddMedicineSecondScreen(
                med = state.med,
                onNext = {
                    viewModel.reduce(NewCourseIntent.UpdateMed(it))
                    navigationState.navigateSingleTo(AddCourseScreens.CourseDetailsScreen.route)
                }, onBackPressed = {
                    navigationState.navController.popBackStack()
                }
            )
        }
        composable(route = AddCourseScreens.CourseDetailsScreen.route) { entry ->
            val viewModel = entry.sharedViewModel<CreateCourseViewModel>(navigationState.navController)
            val state by viewModel.state.collectAsStateWithLifecycle()
            AddCourseMainScreen(
                course = state.course,
                onNext = {
                    viewModel.reduce(NewCourseIntent.UpdateCourse(it))
                    navigationState.navigateSingleTo(AddCourseScreens.CourseDurationScreen.route)
                },
                onBackPressed = {
                    navigationState.navController.popBackStack()
                }
            )
        }
        composable(route = AddCourseScreens.CourseDurationScreen.route) { entry ->
            val viewModel = entry.sharedViewModel<CreateCourseViewModel>(navigationState.navController)
            val state by viewModel.state.collectAsStateWithLifecycle()
            AddNotificationsMainScreen(
                med = state.med,
                onNext = {
                    viewModel.reduce(NewCourseIntent.Finish)
                    navigationState.navController.navigate(AddCourseScreens.CongratulationsScreen.route) {
                        launchSingleTop = true
                        popUpTo(AddCourseScreens.MedDetailsScreen.route) {
                            inclusive = true
                        }
                    }
                },
                onBackPressed = {
                    navigationState.navController.popBackStack()
                }
            )
        }
        composable(route = AddCourseScreens.CongratulationsScreen.route) {
            SuccessMainScreen {
                navigationState.navController.popBackStack()
            }
            BackHandler {
                // back press not allowed because of status bar color changing issue
            }
        }
    }
}