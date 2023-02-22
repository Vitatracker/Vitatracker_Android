package app.mybad.notifier.ui.screens.authorization

import app.mybad.notifier.ui.screens.course.NavItem

sealed class AuthorizationNavItem(
    val route: String
) {
    object authorization : AuthorizationNavItem("Authorization")
    object login : AuthorizationNavItem("login")
    object registration : AuthorizationNavItem("registration")
}