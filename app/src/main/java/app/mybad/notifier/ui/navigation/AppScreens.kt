package app.mybad.notifier.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class AppScreens(val route: String) {

    object Splash : AppScreens(ROUTE_SPLASH)
    object Authorization : AppScreens(ROUTE_AUTHORIZATION)
    object Main : AppScreens(ROUTE_MAIN)
    object AddCourse : AppScreens(ROUTE_ADD_COURSE)
    object NewPassword : AppScreens(ROUTE_NEW_PASSWORD)
    object EditCourse : AppScreens(ROUTE_EDIT_COURSE){
        const val courseIdArg = "courseId"
        val routeWithArgs = "${route}/{${courseIdArg}}"
        val arguments = listOf(
            navArgument(courseIdArg) { type = NavType.LongType }
        )
    }
    private companion object {
        const val ROUTE_SPLASH = "splash"
        const val ROUTE_AUTHORIZATION = "authorization"
        const val ROUTE_MAIN = "main"
        const val ROUTE_ADD_COURSE = "add_course"
        const val ROUTE_EDIT_COURSE = "edit_course"
        const val ROUTE_NEW_PASSWORD = "new_password"
    }
}
