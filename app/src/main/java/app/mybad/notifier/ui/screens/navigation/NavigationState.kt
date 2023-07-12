package app.mybad.notifier.ui.screens.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class NavigationState(val navController: NavHostController) {
    fun navigateTo(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateSingleTo(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun navigateToMain() {
        navController.popBackStack()
        navigateSingleTo(Screen.Main.route)
    }

    fun navigateToAuthorization() {
        navController.popBackStack()
        navigateSingleTo(Screen.Authorization.route)
    }
}

@Composable
fun rememberNavigationState(navController: NavHostController = rememberNavController()): NavigationState {
    return remember {
        NavigationState(navController)
    }
}

fun String.encode(): String {
    return Uri.encode(this)
}