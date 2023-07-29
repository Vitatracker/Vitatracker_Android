package app.mybad.notifier.ui.screens.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.mybad.notifier.ui.screens.calender.CalendarScreen
import app.mybad.notifier.ui.screens.mainscreen.NotificationsScreen
import app.mybad.notifier.ui.screens.mycourses.screens.MyCoursesMainScreen

@Composable
fun MainNavGraph(
    navigationState: NavigationState,
    paddingValues: PaddingValues
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
            MyCoursesMainScreen()
        }
        composable(route = MainScreens.Calendar.route) {
            CalendarScreen()
        }
        settingsNavGraph(navigationState)
    }
}