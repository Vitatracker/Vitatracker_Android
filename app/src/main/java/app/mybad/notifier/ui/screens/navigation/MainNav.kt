package app.mybad.notifier.ui.screens.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.mybad.notifier.ui.screens.calender.CalendarScreen
import app.mybad.notifier.ui.screens.calender.CalendarViewModel
import app.mybad.notifier.ui.screens.newcourse.CreateCourseViewModel
import app.mybad.notifier.ui.screens.mainscreen.StartMainScreen
import app.mybad.notifier.ui.screens.mainscreen.StartMainScreenViewModel
import app.mybad.notifier.ui.screens.mycourses.screens.MyCoursesMainScreen
import app.mybad.notifier.ui.screens.mycourses.MyCoursesNavItem
import app.mybad.notifier.ui.screens.mycourses.MyCoursesViewModel
import app.mybad.notifier.ui.screens.newcourse.NewCourseIntent
import app.mybad.notifier.ui.screens.newcourse.NewCourseNavScreen
import app.mybad.notifier.ui.screens.settings.SettingsNav
import app.mybad.notifier.ui.screens.settings.SettingsViewModel

@Composable
fun MainNav(
    modifier: Modifier = Modifier,
    createCourseVm: CreateCourseViewModel,
    myCoursesVm: MyCoursesViewModel,
    settingsVm: SettingsViewModel,
    calendarVm: CalendarViewModel,
    mainScreenVm: StartMainScreenViewModel
) {
    val navController = rememberNavController()
    var isOnTopLevel by remember { mutableStateOf(true) }
    val calendarState = calendarVm.state.collectAsState()

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
                isOnTopLevel = true
                StartMainScreen(
                    navController = navController,
                    vm = mainScreenVm
                )
            }
            composable(NavItemMain.Courses.route) {
                val myCoursesNavController = rememberNavController()
                val path = myCoursesNavController.currentBackStackEntryAsState()
                MyCoursesMainScreen(
                    modifier = modifier.padding(horizontal = 16.dp),
                    navHostController = myCoursesNavController,
                    vm = myCoursesVm
                )
                isOnTopLevel = path.value?.destination?.route != MyCoursesNavItem.Course.route
            }
            composable(NavItemMain.Calendar.route) {
                CalendarScreen(
                    modifier = modifier.padding(horizontal = 16.dp),
                    meds = calendarState.value.meds,
                    usages = calendarState.value.usages,
                    reducer = { intent -> calendarVm.reducer(intent) }
                )
                isOnTopLevel = true
            }
            composable(NavItemMain.Settings.route) {
                val settingsNavController = rememberNavController()
                isOnTopLevel = true
                SettingsNav(
                    modifier = modifier.padding(horizontal = 16.dp),
                    vm = settingsVm,
                    navController = settingsNavController,
                    onDismiss = { navController.popBackStack() }
                )
            }
            composable(NavItemMain.Add.route) {
                val nc = rememberNavController()
                isOnTopLevel = false
                NewCourseNavScreen(
                    modifier = modifier,
                    navHostController = nc,
                    vm = createCourseVm,
                    onCancel = {
                        navController.popBackStack()
                        createCourseVm.reduce(NewCourseIntent.Drop)
                    },
                    onFinish = { navController.popBackStack() }
                )
            }
        }
    }
}
