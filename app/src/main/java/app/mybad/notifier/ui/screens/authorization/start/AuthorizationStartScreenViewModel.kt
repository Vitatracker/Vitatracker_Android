package app.mybad.notifier.ui.screens.authorization.start

import app.mybad.notifier.ui.base.BaseViewModel

class AuthorizationStartScreenViewModel :
    BaseViewModel<AuthorizationStartScreenContract.Event, AuthorizationStartScreenContract.State, AuthorizationStartScreenContract.Effect>() {
    override fun setInitialState(): AuthorizationStartScreenContract.State = AuthorizationStartScreenContract.State

    override fun handleEvents(event: AuthorizationStartScreenContract.Event) {
        when (event) {
            AuthorizationStartScreenContract.Event.Registration -> setEffect { AuthorizationStartScreenContract.Effect.Navigation.ToRegistration }
            AuthorizationStartScreenContract.Event.SignIn -> setEffect { AuthorizationStartScreenContract.Effect.Navigation.ToAuthorization }
            AuthorizationStartScreenContract.Event.SignInWithGoogle -> {
                signInWithGoogle()
            }
        }
    }

    private fun signInWithGoogle() {
        /* TODO ADD GOOGLE  */
    }
}