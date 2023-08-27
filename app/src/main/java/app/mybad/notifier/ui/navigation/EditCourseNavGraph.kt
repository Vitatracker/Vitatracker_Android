package app.mybad.notifier.ui.navigation

import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import app.mybad.notifier.ui.screens.mycourses.MyCoursesContract
import app.mybad.notifier.ui.screens.mycourses.MyCoursesViewModel
import app.mybad.notifier.ui.screens.mycourses.edit.MyCourseEditNotificationScreen
import app.mybad.notifier.ui.screens.mycourses.edit.MyCourseEditScreen
import app.mybad.notifier.ui.screens.newcourse.CreateCourseViewModel

fun NavGraphBuilder.editCourseNavGraph(navigationState: NavigationState) {
    navigation(
        route = AppScreens.EditCourse.routeWithArgs,
        arguments = AppScreens.EditCourse.arguments,
        startDestination = EditCourseScreens.CourseEdit.route,
    ) {

        composable(route = EditCourseScreens.CourseEdit.route) { navBackStackEntry ->
            val viewModel = navBackStackEntry
                .sharedViewModel<MyCoursesViewModel>(navigationState.navController)

            val arguments = requireNotNull(navBackStackEntry.arguments)
            val courseId = arguments.getLong(AppScreens.EditCourse.courseIdArg)
            Log.w(
                "VTTAG",
                "EditCourseNavGraph::EditCourseScreens.CourseEdit.route: courseId=$courseId"
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
        composable(route = EditCourseScreens.NotificationEdit.route) { navBackStackEntry ->
            val viewModel = navBackStackEntry
                .sharedViewModel<MyCoursesViewModel>(navigationState.navController)

            MyCourseEditNotificationScreen(
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
