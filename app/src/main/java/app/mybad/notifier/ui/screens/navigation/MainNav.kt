package app.mybad.notifier.ui.screens.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.notifier.ui.screens.course.NewCourseNav
import app.mybad.notifier.ui.screens.settings.SettingsNav

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNav(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {

    val userModel = UserDomainModel()
    var isOnTopLevel by remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBar(
                navController = navController,
                isVisible = isOnTopLevel
            )
        }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = NavItemMain.Notifications.route
        ) {
            composable(NavItemMain.Notifications.route) {
                Text("notifications")
                isOnTopLevel = true
            }
            composable(NavItemMain.Courses.route) {
                Text("courses")
                isOnTopLevel = true
            }
            composable(NavItemMain.Calendar.route) {
                Text("calendar")
                isOnTopLevel = true
            }
            composable(NavItemMain.Settings.route) {
                val settingsNavController = rememberNavController()
                isOnTopLevel = true
                SettingsNav(
                    modifier = modifier,
                    navController = settingsNavController,
                    userModel = userModel,
                    onDismiss = { navController.popBackStack() }
                )
            }
            composable(NavItemMain.Add.route) {
                val settingsNavController = rememberNavController()
                isOnTopLevel = false
                NewCourseNav(
                    modifier = modifier,
                    userId = userModel.id,
                    navController = settingsNavController,
                    onDismiss = { navController.popBackStack() },
                    onFinish = { }
                )
            }
        }
    }
}