package app.mybad.notifier.ui.screens.mainscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import app.mybad.notifier.ui.screens.calender.CalendarScreen
import app.mybad.notifier.ui.screens.mycourses.screens.MyCoursesMainScreen
import app.mybad.notifier.ui.screens.navigation.BottomNavBar
import app.mybad.notifier.ui.screens.navigation.MainNavGraph
import app.mybad.notifier.ui.screens.navigation.rememberNavigationState

@Composable
fun MainScreen(
    notificationsScreenContent: @Composable () -> Unit,
    coursesScreenContent: @Composable () -> Unit,
    calendarScreenContent: @Composable () -> Unit,
    settingsScreenContent: @Composable () -> Unit,
    onAddClicked: () -> Unit
) {
    val navigationState = rememberNavigationState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBar(navController = navigationState.navController,
                onItemSelected = {
                    navigationState.navigateTo(it.route)
                },
                onAddItemClicked = {
                    onAddClicked()
                })
        }
    ) { paddingValues ->
        MainNavGraph(
            navController = navigationState.navController,
            paddingValues = paddingValues,
            notificationScreenContent = {
                notificationsScreenContent()
            },
            coursesScreenContent = {
                coursesScreenContent()
            },
            calendarScreenContent = {
                calendarScreenContent()
            },
            settingsScreenContent = {
                settingsScreenContent()
            }
        )
    }
}
