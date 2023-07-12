package app.mybad.notifier.ui.screens.navigation

sealed class AuthorizationScreens(val route: String) {
    object ChooseMode : AuthorizationScreens(ROUTE_SELECT_AUTHORIZATION_MODE)
    object Login : AuthorizationScreens(ROUTE_LOGIN)
    object Registration : AuthorizationScreens(ROUTE_REGISTRATION)
    object PasswordRecovery : AuthorizationScreens(ROUTE_PASSWORD_RECOVERY)
    object NewPassword : AuthorizationScreens(ROUTE_NEW_PASSWORD)

    private companion object {
        const val ROUTE_SELECT_AUTHORIZATION_MODE = "authorization_select_authorization_mode"
        const val ROUTE_LOGIN = "authorization_login"
        const val ROUTE_REGISTRATION = "authorization_registration"
        const val ROUTE_PASSWORD_RECOVERY = "authorization_password_recovery"
        const val ROUTE_NEW_PASSWORD = "authorization_new_password"
    }
}