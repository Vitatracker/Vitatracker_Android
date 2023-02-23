package app.mybad.notifier.ui.screens.authorization.navigation

sealed class AuthorizationNavItem(
    val route: String
) {
    object Welcome : AuthorizationNavItem("welcome")
    object Authorization : AuthorizationNavItem("authorization")
    object Login : AuthorizationNavItem("login")
    object Registration : AuthorizationNavItem("registration")
    object LoginWithFacebook : AuthorizationNavItem("loginWithFacebook")
    object LoginWithGoogle : AuthorizationNavItem("loginWithGoogle")
    object RecoveryPassword : AuthorizationNavItem("recoveryPassword")
    object NewPassword : AuthorizationNavItem("newPassword")
}