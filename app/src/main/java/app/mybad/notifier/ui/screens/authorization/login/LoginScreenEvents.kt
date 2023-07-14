package app.mybad.notifier.ui.screens.authorization.login

sealed class LoginScreenEvents {
    object LoginSuccessful : LoginScreenEvents()
    object InvalidCredentials : LoginScreenEvents()
}