package app.mybad.notifier.ui.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.mybad.notifier.ui.navigation.AppScreens
import app.mybad.notifier.ui.navigation.BottomNavBar
import app.mybad.notifier.ui.navigation.MainNavGraph
import app.mybad.notifier.ui.navigation.rememberNavigationState

@Composable
fun MainScreen(
    navigateUp: (String) -> Unit = {},
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
                    navigateUp(AppScreens.AddCourse.route)
                })
        }
    ) { paddingValues ->
        MainNavGraph(
            navigationState = navigationState,
            paddingValues = paddingValues,
            navigateUp = navigateUp,
        )
    }
}
