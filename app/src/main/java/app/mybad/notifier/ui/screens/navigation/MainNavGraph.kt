package app.mybad.notifier.ui.screens.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.mybad.notifier.ui.screens.calender.CalendarScreen
import app.mybad.notifier.ui.screens.mainscreen.NotificationsScreen
import app.mybad.notifier.ui.screens.mycourses.MyCoursesScreen
import app.mybad.notifier.ui.screens.mycourses.MyCoursesScreenContract
import app.mybad.notifier.ui.screens.mycourses.MyCoursesScreenViewModel

@Composable
fun MainNavGraph(
    navigationState: NavigationState,
    paddingValues: PaddingValues,
    toAuthorizationRequested: () -> Unit
) {
    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navigationState.navController,
        startDestination = MainScreens.Notifications.route
    ) {
        composable(route = MainScreens.Notifications.route) {
            NotificationsScreen()
        }
        composable(route = MainScreens.Courses.route) {
            val viewModel: MyCoursesScreenViewModel = hiltViewModel()
            MyCoursesScreen(
                state = viewModel.viewState.value,
                events = viewModel.effect,
                onEventSent = { viewModel.setEvent(it) },
                onNavigationRequested = { navigationEffect ->
                    when (navigationEffect) {
                        is MyCoursesScreenContract.Effect.Navigation.ToEditCourse -> {}
                    }
                }
            )
        }
        composable(route = MainScreens.Calendar.route) {
            CalendarScreen()
        }
        settingsNavGraph(navigationState) {
            toAuthorizationRequested()
        }
    }
}