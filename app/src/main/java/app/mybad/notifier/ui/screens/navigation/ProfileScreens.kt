package app.mybad.notifier.ui.screens.navigation

sealed class ProfileScreens(val route: String) {
    object MainScreen: ProfileScreens(ROUTE_MAIN)


    private companion object {
        const val ROUTE_MAIN = "profile_main"
    }
}
