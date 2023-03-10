package app.mybad.notifier.ui.screens.newcourse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import app.mybad.notifier.R
import app.mybad.notifier.ui.screens.course.CreateCourseViewModel
import app.mybad.notifier.ui.screens.newcourse.screens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCourseNavScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    vm: CreateCourseViewModel,
    onCancel: () -> Unit,
    onFinish: () -> Unit
) {

    val state = vm.state.collectAsState()
    val dest = navHostController.currentBackStackEntryAsState()
    val h = stringResource(R.string.add_med_h)
    var title by remember { mutableStateOf(h) }

    Column {
        if(dest.value?.destination?.route != NewCourseNavItem.Success.route) TopAppBar(
            title = {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(end = 24.dp)
                )
            }
        )
        NavHost(
            modifier = modifier,
            navController = navHostController,
            startDestination = NewCourseNavItem.AddMedicineMain.route
        ) {
            composable(NewCourseNavItem.AddMedicineMain.route) {
                title = stringResource(R.string.add_med_h)
                AddMedicineMainScreen(
                    med = state.value.med,
                    reducer = vm::reduce,
                    onNext = { navHostController.navigate(NewCourseNavItem.AddCourse.route) },
                    onBack = onCancel::invoke,
                )
            }
            composable(NewCourseNavItem.AddCourse.route) {
                title = stringResource(R.string.add_course_h)
                AddCourseMainScreen(
                    course = state.value.course,
                    reducer = vm::reduce,
                    onNext = { navHostController.navigate(NewCourseNavItem.AddNotifications.route) },
                    onBack = { navHostController.popBackStack() },
                )
            }
            composable(NewCourseNavItem.AddNotifications.route) {
                title = stringResource(R.string.add_course_notifications_time)
                AddNotificationsMainScreen(
                    med = state.value.med,
                    reducer = vm::reduce,
                    onNext = { navHostController.navigate(NewCourseNavItem.Success.route) },
                    onBack = { navHostController.popBackStack() },
                )
            }
            composable(NewCourseNavItem.Success.route) {
                SuccessMainScreen(
                    onGo = onFinish::invoke
                )
            }
        }
    }
}