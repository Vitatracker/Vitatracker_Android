package app.mybad.notifier.ui.screens.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

fun NavGraphBuilder.mainNavGraph(navigationState: NavigationState) {
    navigation(startDestination = MainScreens.Notifications.route, route = Screen.Main.route) {

    }
}

@Composable
fun MainNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    notificationScreenContent: @Composable () -> Unit,
    coursesScreenContent: @Composable () -> Unit,
    calendarScreenContent: @Composable () -> Unit,
    settingsScreenContent: @Composable () -> Unit,
) {
    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        startDestination = MainScreens.Notifications.route
    ) {
        composable(route = MainScreens.Notifications.route) {
            notificationScreenContent()
        }
        composable(route = MainScreens.Courses.route) {
            coursesScreenContent()
        }
        composable(route = MainScreens.Calendar.route) {
            calendarScreenContent()
        }
        composable(route = MainScreens.Settings.route) {
            settingsScreenContent()
        }
    }
}