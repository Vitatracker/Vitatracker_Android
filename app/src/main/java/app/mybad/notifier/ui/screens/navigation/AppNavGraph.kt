package app.mybad.notifier.ui.screens.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.mybad.notifier.ui.screens.calender.CalendarScreen
import app.mybad.notifier.ui.screens.mainscreen.MainScreen
import app.mybad.notifier.ui.screens.mainscreen.NotificationsScreen
import app.mybad.notifier.ui.screens.mycourses.screens.MyCoursesMainScreen
import app.mybad.notifier.ui.screens.splash.SplashScreen

@Composable
fun AppNavGraph() {
    val navigationState = rememberNavigationState()
    Scaffold { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navigationState.navController,
            startDestination = Screen.Splash.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    proceedToMain = {
                        navigationState.navigateToMain()
                    },
                    proceedToAuthorization = {
                        navigationState.navigateToAuthorization()
                    }
                )
            }
            authorizationNavGraph(navigationState)
            composable(Screen.Main.route) {
                MainScreen(
                    notificationsScreenContent = {
                        NotificationsScreen()
                    },
                    coursesScreenContent = {
                        MyCoursesMainScreen()
                    },
                    calendarScreenContent = {
                        CalendarScreen()
                    },
                    settingsScreenContent = {
                        Text("Settings")
                    },
                    onAddClicked = {
                        navigationState.navigateSingleTo(Screen.AddCourse.route)
                    }
                )
            }
            addCourseNavGraph(navigationState)
        }
    }

}