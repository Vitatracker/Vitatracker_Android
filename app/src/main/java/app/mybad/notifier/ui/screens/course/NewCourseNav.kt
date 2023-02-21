package app.mybad.notifier.ui.screens.course

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import app.mybad.domain.models.med.MedDomainModel

@Composable
fun NewCourseNav(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {


    var newMed by remember { mutableStateOf(MedDomainModel()) }

    NavHost(
        navController = navController,
        startDestination = NavItem.AddMed.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(NavItem.AddMed.route) {
            AddMedScreen(
                onNext = {
                    navController.navigate(NavItem.AddCourse.route)
                    newMed = it
                    Log.w("NCN_", "$newMed")
                },
                onBack = {  },
            )
        }
        composable(NavItem.AddCourse.route) {
            AddCourse(
                onNext = { navController.navigate(NavItem.NextCourse.route)},
                onBack = { navController.popBackStack() },
            )
        }
        composable(NavItem.NextCourse.route) {
            NextCourse(
                onNext = {  },
                onBack = { navController.popBackStack() },
            )
        }
    }
}