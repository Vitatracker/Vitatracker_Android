package app.mybad.notifier.ui.screens.navigation

sealed class Screen(val route: String) {

    object Splash : Screen(ROUTE_SPLASH)
    object Authorization : Screen(ROUTE_AUTHORIZATION)
    object Main : Screen(ROUTE_MAIN)

    private companion object {
        const val ROUTE_SPLASH = "splash"
        const val ROUTE_AUTHORIZATION = "authorization"
        const val ROUTE_AUTHORIZATION_SELECT_AUTHORIZATION_MODE = "authorization_select_authorization_mode"
        const val ROUTE_AUTHORIZATION_LOGIN = "authorization_login"
        const val ROUTE_AUTHORIZATION_REGISTRATION = "authorization_registration"
        const val ROUTE_MAIN = "main"
    }
}
