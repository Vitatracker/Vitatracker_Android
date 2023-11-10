package app.mybad.notifier.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class AppScreens(val route: String) {

    object Splash : AppScreens(ROUTE_SPLASH)
    object Authorization : AppScreens(ROUTE_AUTHORIZATION)
    object NotificationRequest : AppScreens(ROUTE_NOTIFICATION_REQUEST)
    object NotificationsSettings : AppScreens(ROUTE_NOTIFICATION_SETTINGS)
    object Main : AppScreens(ROUTE_MAIN)
    object AddCourse : AppScreens(ROUTE_ADD_COURSE)
    object EditCourse : AppScreens(ROUTE_EDIT_COURSE){
        const val courseIdArg = "courseId"
        val routeWithArgs = "${route}/{${courseIdArg}}"
        val arguments = listOf(
            navArgument(courseIdArg) { type = NavType.LongType }
        )
    }
    object Profile : AppScreens(ROUTE_PROFILE)

    private companion object {
        const val ROUTE_SPLASH = "splash"
        const val ROUTE_AUTHORIZATION = "authorization"
        const val ROUTE_NOTIFICATION_REQUEST = "notification_request"
        const val ROUTE_NOTIFICATION_SETTINGS = "settings_notifications_system"
        const val ROUTE_MAIN = "main"
        const val ROUTE_ADD_COURSE = "add_course"
        const val ROUTE_EDIT_COURSE = "edit_course"
        const val ROUTE_PROFILE = "profile"
    }
}
