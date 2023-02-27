package app.mybad.notifier.ui.screens.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.notifier.StartMainScreen
import app.mybad.notifier.ui.screens.calender.CalendarScreen
import app.mybad.notifier.ui.screens.calender.CalendarViewModel
import app.mybad.notifier.ui.screens.course.CreateCourseViewModel
import app.mybad.notifier.ui.screens.course.composable.NewCourseNav
import app.mybad.notifier.ui.screens.mycourses.MyCourses
import app.mybad.notifier.ui.screens.mycourses.MyCoursesViewModel
import app.mybad.notifier.ui.screens.settings.SettingsNav
import app.mybad.notifier.ui.screens.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNav(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    createCourseVm: CreateCourseViewModel,
    myCoursesVm: MyCoursesViewModel,
    settingsVm: SettingsViewModel,
    calendarVm: CalendarViewModel
) {

    val userModel = UserDomainModel()
    var isOnTopLevel by remember { mutableStateOf(true) }

    val coursesState = myCoursesVm.state.collectAsState()
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
                StartMainScreen(navController = navController)
                isOnTopLevel = true
            }
            composable(NavItemMain.Courses.route) {
                MyCourses(
                    modifier = modifier,
                    courses = coursesState.value.courses,
                    meds = coursesState.value.meds,
                    reducer = myCoursesVm::reduce
                )
                isOnTopLevel = true
            }
            composable(NavItemMain.Calendar.route) {
                CalendarScreen(
                    modifier = modifier,
                    courses = calendarState.value.courses,
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
                    modifier = modifier,
                    vm = settingsVm,
                    navController = settingsNavController,
                    onDismiss = { navController.popBackStack() }
                )
            }
            composable(NavItemMain.Add.route) {
                val settingsNavController = rememberNavController()
                isOnTopLevel = false
                NewCourseNav(
                    modifier = modifier,
                    vm = createCourseVm,
                    userId = userModel.id,
                    navController = settingsNavController,
                    onDismiss = { navController.popBackStack() },
                )
            }
        }
    }
}