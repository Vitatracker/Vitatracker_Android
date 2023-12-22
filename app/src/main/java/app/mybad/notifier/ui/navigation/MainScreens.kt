package app.mybad.notifier.ui.navigation

import app.mybad.theme.R

sealed class MainScreens(val route: String, val label: Int, val icon: Int) {
    data object Notifications : MainScreens(
        route = ROUTE_NOTIFICATIONS,
        label = R.string.navigation_notifications,
        icon = R.drawable.notif
    )

    data object Courses : MainScreens(
        route = ROUTE_COURSES,
        label = R.string.navigation_courses,
        icon = R.drawable.pill
    )

    data object Calendar : MainScreens(
        route = ROUTE_CALENDAR,
        label = R.string.navigation_calendar,
        icon = R.drawable.calendar
    )

    data object Settings : MainScreens(
        route = ROUTE_SETTINGS,
        label = R.string.navigation_settings_main,
        icon = R.drawable.sett
    )


    private companion object {
        const val ROUTE_NOTIFICATIONS = "main_notifications"
        const val ROUTE_COURSES = "main_courses"
        const val ROUTE_CALENDAR = "main_calendar"
        const val ROUTE_SETTINGS = "main_settings"
    }
}
