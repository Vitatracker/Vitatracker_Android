package app.mybad.notifier.ui.screens.mainscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import app.mybad.notifier.ui.screens.calender.CalendarScreen
import app.mybad.notifier.ui.screens.navigation.BottomNavBar
import app.mybad.notifier.ui.screens.navigation.MainNavGraph
import app.mybad.notifier.ui.screens.navigation.rememberNavigationState

@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBar(navController = navigationState.navController,
                onItemSelected = {
                    navigationState.navigateTo(it.route)
                },
                onAddItemClicked = {})
        }
    ) { paddingValues ->
        MainNavGraph(
            navController = navigationState.navController,
            paddingValues = paddingValues,
            notificationScreenContent = {
                NotificationsScreen()
            },
            coursesScreenContent = {
                Text("Courses")
            },
            calendarScreenContent = {
                CalendarScreen(usages = listOf(), meds = listOf())
            },
            settingsScreenContent = {
                Text("Settings")
            }
        )
    }
}
