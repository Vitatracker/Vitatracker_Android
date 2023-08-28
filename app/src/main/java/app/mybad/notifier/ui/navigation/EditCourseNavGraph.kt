package app.mybad.notifier.ui.navigation

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import app.mybad.notifier.ui.screens.mycoursesedit.MyCourseEditNotificationScreen
import app.mybad.notifier.ui.screens.mycoursesedit.MyCourseEditScreen
import app.mybad.notifier.ui.screens.mycoursesedit.MyCoursesEditContract
import app.mybad.notifier.ui.screens.mycoursesedit.MyCoursesEditViewModel

fun NavGraphBuilder.editCourseNavGraph(navigationState: NavigationState) {
    navigation(
        route = AppScreens.EditCourse.routeWithArgs,
        arguments = AppScreens.EditCourse.arguments,
        startDestination = EditCourseScreens.CourseEdit.route,
    ) {

        composable(route = EditCourseScreens.CourseEdit.route) { navBackStackEntry ->
            val viewModel = navBackStackEntry
                .sharedViewModel<MyCoursesEditViewModel>(navigationState.navController)

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
                        MyCoursesEditContract.Effect.Navigation.NotificationEditing -> {
                            navigationState.navigateTo(EditCourseScreens.NotificationEdit.route)
                        }

                        MyCoursesEditContract.Effect.Navigation.Back -> {
                            Log.w(
                                "VTTAG",
                                "AppNavGraph::MyCourseEditScreen: navigate->popBackStack"
                            )
                            navigationState.navController.popBackStack()
                        }
                    }
                }
            )
        }
        composable(route = EditCourseScreens.NotificationEdit.route) { navBackStackEntry ->
            val viewModel = navBackStackEntry
                .sharedViewModel<MyCoursesEditViewModel>(navigationState.navController)
            // state должен быть уже загружен
            MyCourseEditNotificationScreen(
                state = viewModel.viewState.value,
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationAction ->
                    when (navigationAction) {
                        MyCoursesEditContract.Effect.Navigation.NotificationEditing -> {}

                        MyCoursesEditContract.Effect.Navigation.Back -> {
                            Log.w(
                                "VTTAG",
                                "EditCourseNavGraph::MyCourseEditNotificationScreen: navigate->popBackStack"
                            )
                            navigationState.navController.popBackStack()
                        }
                    }
                }
            )
        }
    }
}
