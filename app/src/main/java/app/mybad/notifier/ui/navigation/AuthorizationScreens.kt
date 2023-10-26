package app.mybad.notifier.ui.navigation

sealed class AuthorizationScreens(val route: String) {
    object ChooseMode : AuthorizationScreens(ROUTE_SELECT_AUTHORIZATION_MODE)
    object Login : AuthorizationScreens(ROUTE_LOGIN)
    object Registration : AuthorizationScreens(ROUTE_REGISTRATION)
    object PasswordRecovery : AuthorizationScreens(ROUTE_PASSWORD_RECOVERY)
    object NewPassword : AuthorizationScreens(ROUTE_NEW_PASSWORD) {
        const val TOKEN_ARG = "token"
        const val EMAIL_ARG = "userEmail"
        private const val ROUTE_FOR_ARGS = "authorization_new_password"

        fun getRouteWithArgs(token: String, email: String): String {
            return "$ROUTE_FOR_ARGS/$token/$email"
        }
    }

    object RecoveryCodeVerification : AuthorizationScreens(ROUTE_RECOVERY_CODE_VERIFICATION) {
        const val EMAIL_ARG = "userEmail"
        private const val ROUTE_FOR_ARGS = "authorization_recovery_code_verification"

        fun getRouteWithArgs(email: String): String {
            return "$ROUTE_FOR_ARGS/$email"
        }
    }

    private companion object {
        const val ROUTE_SELECT_AUTHORIZATION_MODE = "authorization_select_authorization_mode"
        const val ROUTE_LOGIN = "authorization_login"
        const val ROUTE_REGISTRATION = "authorization_registration"
        const val ROUTE_PASSWORD_RECOVERY = "authorization_password_recovery"
        const val ROUTE_NEW_PASSWORD = "authorization_new_password/{${NewPassword.TOKEN_ARG}}/{${NewPassword.EMAIL_ARG}}"
        const val ROUTE_RECOVERY_CODE_VERIFICATION = "authorization_recovery_code_verification/{${RecoveryCodeVerification.EMAIL_ARG}}"
    }
}
