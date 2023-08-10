package app.mybad.notifier.ui.screens.navigation

sealed class SettingsScreens(val route: String) {
    object Navigation : SettingsScreens("settings_navigation")
    object Profile : SettingsScreens("settings_profile")
    object LeaveYourWishes :
        SettingsScreens("settings_leave_your_wishes")

    object PasswordChange :
        SettingsScreens("settings_password_change")

    object Notifications : SettingsScreens("settings_notifications")
    object About : SettingsScreens("settings_about")
    object AboutOurTeam : SettingsScreens("settings_about_our_team")
}