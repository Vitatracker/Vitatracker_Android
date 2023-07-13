package app.mybad.notifier.ui.screens.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.med.MedDomainModel
import app.mybad.notifier.ui.screens.newcourse.screens.AddCourseMainScreen
import app.mybad.notifier.ui.screens.newcourse.screens.AddMedicineFirstScreen
import app.mybad.notifier.ui.screens.newcourse.screens.AddMedicineSecondScreen
import com.google.gson.Gson

fun NavGraphBuilder.addCourseNavGraph(navigationState: NavigationState) {
    navigation(startDestination = AddCourseScreens.FirstScreen.route, route = Screen.AddCourse.route) {
        composable(route = AddCourseScreens.FirstScreen.route) {
            val med = it.savedStateHandle.get<MedDomainModel>(AddCourseScreens.KEY_MED) ?: MedDomainModel()
            AddMedicineFirstScreen(
                med = med,
                onNext = {
                    navigationState.navigateSingleTo(AddCourseScreens.SecondScreen.getRouteWithArgs(it))
                },
                onBackPressed = {
                    navigationState.navController.popBackStack()
                }
            )
        }
        composable(
            route = AddCourseScreens.SecondScreen.route,
            arguments = listOf(
                navArgument(AddCourseScreens.KEY_MED) {
                    type = NavType.StringType
                }
            )) {
            val medJson = it.arguments?.getString(AddCourseScreens.KEY_MED) ?: ""
            val med = Gson().fromJson(medJson, MedDomainModel::class.java)
            AddMedicineSecondScreen(
                med = med,
                onNext = {
                    navigationState.navigateSingleTo(AddCourseScreens.ThirdScreen.getRouteWithArgs(it))
                }, onBackPressed = { updatedMed ->
                    navigationState.navController.previousBackStackEntry?.savedStateHandle?.set(AddCourseScreens.KEY_MED, updatedMed)
                    navigationState.navController.popBackStack()
                }
            )
        }
        composable(
            route = AddCourseScreens.ThirdScreen.route,
            arguments = listOf(
                navArgument(AddCourseScreens.KEY_MED) {
                    type = NavType.StringType
                }
            )) {
            val medJson = it.arguments?.getString(AddCourseScreens.KEY_MED) ?: ""
            val med = Gson().fromJson(medJson, MedDomainModel::class.java)
            AddCourseMainScreen(
                course = CourseDomainModel(),
                onNext = {},
                onBackPressed = {
                    navigationState.navController.popBackStack()
                }
            )
        }
    }
}