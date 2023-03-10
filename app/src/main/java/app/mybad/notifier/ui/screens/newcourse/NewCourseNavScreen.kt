package app.mybad.notifier.ui.screens.newcourse

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.mybad.notifier.ui.screens.course.CreateCourseViewModel
import app.mybad.notifier.ui.screens.newcourse.composable.*

@Composable
fun NewCourseNavScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    vm: CreateCourseViewModel,
    onCancel: () -> Unit,
    onFinish: () -> Unit
) {

    val state = vm.state.collectAsState()

    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = NewCourseNavItem.AddMedicineMain.route
    ) {
        composable(NewCourseNavItem.AddMedicineMain.route) {
            AddMedicineMainScreen(
                med = state.value.med,
                reducer = vm::reduce,
                onNext = { navHostController.navigate(NewCourseNavItem.AddMedicineDetails.route) },
                onBack = onCancel::invoke,
            )
        }
        composable(NewCourseNavItem.AddMedicineDetails.route) {
            AddMedicineDetailsScreen(
                med = state.value.med,
                reducer = vm::reduce,
                onNext = { navHostController.navigate(NewCourseNavItem.AddCourse.route) },
                onBack = { navHostController.popBackStack() },
            )
        }
        composable(NewCourseNavItem.AddCourse.route) {
            AddCourseMainScreen(
                course = state.value.course,
                reducer = vm::reduce,
                onNext = { navHostController.navigate(NewCourseNavItem.AddNotifications.route) },
                onBack = { navHostController.popBackStack() },
            )
        }
        composable(NewCourseNavItem.AddNotifications.route) {
            AddNotificationsMainScreen(
                usages = state.value.usages,
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