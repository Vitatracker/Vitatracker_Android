package app.mybad.notifier.ui.screens.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import app.mybad.notifier.R

sealed class NavItemMain(
    val route: String,
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
) {
    object Courses : NavItemMain(route = "Courses", label = R.string.navigation_courses, icon = R.drawable.pill)
    object Add : NavItemMain(route = "Add", label = R.string.navigation_add, icon = R.drawable.settings)
    object Notifications : NavItemMain(route = "Notifications", label = R.string.navigation_notifications, icon = R.drawable.notif)
    object Settings : NavItemMain(route = "Settings", label = R.string.navigation_settings_main, icon = R.drawable.sett)
    object Calendar : NavItemMain(route = "Calendar", label = R.string.navigation_calendar, icon = R.drawable.calendar)
}