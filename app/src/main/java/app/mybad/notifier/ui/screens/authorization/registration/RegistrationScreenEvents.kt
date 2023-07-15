package app.mybad.notifier.ui.screens.authorization.registration

sealed class RegistrationScreenEvents {
    object RegistrationSuccessful : RegistrationScreenEvents()
    object InvalidCredentials : RegistrationScreenEvents()
    object PasswordsMismatch : RegistrationScreenEvents()
}