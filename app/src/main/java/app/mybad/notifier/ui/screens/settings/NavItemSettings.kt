package app.mybad.notifier.ui.screens.settings

import androidx.annotation.StringRes
import app.mybad.notifier.R

sealed class NavItemSettings(
    val route: String,
    @StringRes val stringId: Int
) {
    object Navigation : NavItemSettings("settings_navigation", R.string.navigation_settings_main)
    object Profile : NavItemSettings("settings_profile", R.string.navigation_settings_profile)
    object LeaveYourWishes :
        NavItemSettings("settings_leave_your_wishes", R.string.settings_leave_your_wishes)

    object PasswordChange :
        NavItemSettings("settings_password_change", R.string.settings_change_password)
    object Notifications : NavItemSettings("settings_notifications", R.string.settings_notifications)
    object About : NavItemSettings("settings_about", R.string.settings_about)
    object OurContacts : NavItemSettings("settings_about_our_contacts", R.string.settings_our_contacts)
}
