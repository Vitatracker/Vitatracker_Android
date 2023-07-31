package app.mybad.notifier.ui.screens.mainscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.mybad.notifier.ui.screens.navigation.BottomNavBar
import app.mybad.notifier.ui.screens.navigation.MainNavGraph
import app.mybad.notifier.ui.screens.navigation.rememberNavigationState

@Composable
fun MainScreen(
    onAddClicked: () -> Unit = {},
    toAuthorizationRequested: () -> Unit = {}
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
            navigationState = navigationState,
            paddingValues = paddingValues,
            toAuthorizationRequested = toAuthorizationRequested
        )
    }
}
