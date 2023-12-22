package app.mybad.notifier.ui.navigation

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
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
    var calendarScreenToday = rememberSaveable { true }
    var notificationsScreenToday = rememberSaveable { false }

    NavHost(
        modifier = Modifier
            .padding(
                start = 0.dp,
                top = 0.dp,
                end = 0.dp,
                bottom = 80.dp,
            ),
        navController = navigationState.navController,
        startDestination = MainScreens.Notifications.route,
        enterTransition = {
            slideInHorizontally(
                animationSpec = tween(500),
                initialOffsetX = { it },
            ) + fadeIn(animationSpec = tween(500))
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(500),
            ) + fadeOut(animationSpec = tween(500))
        },
    ) {
        composable(route = MainScreens.Notifications.route) {

            calendarScreenToday = true

            val viewModel: MainViewModel = hiltViewModel()
            if (notificationsScreenToday) {
                viewModel.setToday()
                notificationsScreenToday = false
            }

            MainNotificationScreen(
                state = viewModel.viewState.value,
                update = viewModel.update,
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

            calendarScreenToday = true
            notificationsScreenToday = true

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

            notificationsScreenToday = true

            val viewModel: CalendarViewModel = hiltViewModel()
            if (calendarScreenToday) {
                viewModel.setToday()
                calendarScreenToday = false
            }
            Log.w(
                "VTTAG",
                "MainNavGraph::CalendarViewModel: CalendarViewModel"
            )

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
        settingsNavGraph(navigationState, navigateUp) {
            calendarScreenToday = true
            notificationsScreenToday = true
        }
    }
}
