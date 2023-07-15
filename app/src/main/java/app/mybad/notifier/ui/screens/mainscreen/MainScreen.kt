package app.mybad.notifier.ui.screens.mainscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import app.mybad.notifier.ui.screens.navigation.BottomNavBar
import app.mybad.notifier.ui.screens.navigation.MainNavGraph
import app.mybad.notifier.ui.screens.navigation.rememberNavigationState

@Preview
@Composable
fun MainScreen(
    notificationsScreenContent: @Composable () -> Unit = {},
    coursesScreenContent: @Composable () -> Unit = {},
    calendarScreenContent: @Composable () -> Unit = {},
    settingsScreenContent: @Composable () -> Unit = {},
    onAddClicked: () -> Unit = {}
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
