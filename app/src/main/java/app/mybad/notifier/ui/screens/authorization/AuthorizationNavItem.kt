package app.mybad.notifier.ui.screens.authorization

import app.mybad.notifier.ui.screens.course.NavItem

sealed class AuthorizationNavItem(
    val route: String
) {
    object Authorization : AuthorizationNavItem("authorization")
    object Login : AuthorizationNavItem("login")
    object Registration : AuthorizationNavItem("registration")
    object LoginWithFacebook : AuthorizationNavItem("loginWithFacebook")
    object LoginWithGoogle : AuthorizationNavItem("loginWithGoogle")
}