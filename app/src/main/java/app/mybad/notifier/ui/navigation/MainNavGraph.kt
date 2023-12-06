package app.mybad.notifier.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.mybad.notifier.ui.screens.calender.CalendarContract
import app.mybad.notifier.ui.screens.calender.CalendarScreen
import app.mybad.notifier.ui.screens.calender.CalendarViewModel
import app.mybad.notifier.ui.screens.main.MainContract
import app.mybad.notifier.ui.screens.main.MainNotificationScreen
import app.mybad.notifier.ui.screens.main.MainViewModel
import app.mybad.notifier.ui.screens.mycourses.MyCoursesContract
import app.mybad.notifier.ui.screens.mycourses.MyCoursesScreen
import app.mybad.notifier.ui.screens.mycourses.MyCoursesViewModel

@Composable
fun MainNavGraph(
    navigationState: NavigationState,
    paddingValues: PaddingValues,
    navigateUp: (String) -> Unit = {},
) {
    NavHost(
        modifier = Modifier.padding(
            start = 0.dp,
            end = 0.dp,
            top = paddingValues.calculateTopPadding(),
            bottom = 0.dp
        ),
        navController = navigationState.navController,
        startDestination = MainScreens.Notifications.route
    ) {
        composable(route = MainScreens.Notifications.route) {

            val viewModel: MainViewModel = hiltViewModel()

            MainNotificationScreen(
                state = viewModel.viewState.value,
                dateTime = viewModel.dateTime,
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationAction ->
                    when (navigationAction) {
                        is MainContract.Effect.Navigation.ToAuthorization -> {
                            navigateUp(AppScreens.Authorization.route)
                        }
                    }
                }
            )
        }
        composable(route = MainScreens.Courses.route) {

            val viewModel: MyCoursesViewModel = hiltViewModel()

            MyCoursesScreen(
                state = viewModel.viewState.value,
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationAction ->
                    when (navigationAction) {
                        is MyCoursesContract.Effect.Navigation.ToCourseEditing -> {
                            Log.w(
                                "VTTAG",
                                "MainNavGraph::StartMyCoursesScreen: ToEditCourse course=${navigationAction.courseId}"
                            )
                            navigateUp("${AppScreens.EditCourse.route}/${navigationAction.courseId}")
                        }

                        MyCoursesContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }
                    }
                }
            )
        }
        composable(route = MainScreens.Calendar.route) {

            val viewModel: CalendarViewModel = hiltViewModel()

            CalendarScreen(
                state = viewModel.viewState.value,
                dateUpdate = viewModel.dateUpdate,
                effectFlow = viewModel.effect,
                sendEvent = viewModel::setEvent,
                navigation = { navigationAction ->
                    when (navigationAction) {
                        CalendarContract.Effect.Navigation.Back -> {
                            navigationState.navController.popBackStack()
                        }
                    }
                }
            )
        }
        //route = MainScreens.Settings.route
        settingsNavGraph(navigationState, navigateUp)
    }
}
