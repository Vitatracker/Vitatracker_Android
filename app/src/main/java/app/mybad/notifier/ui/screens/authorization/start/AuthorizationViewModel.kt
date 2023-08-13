package app.mybad.notifier.ui.screens.authorization.start

import app.mybad.notifier.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(

) : BaseViewModel<AuthorizationContract.Event,
    AuthorizationContract.State,
    AuthorizationContract.Effect>() {

    override fun setInitialState() = AuthorizationContract.State

    override fun handleEvents(event: AuthorizationContract.Event) {
        when (event) {
            AuthorizationContract.Event.Registration -> {
                setEffect {
                    AuthorizationContract.Effect.Navigation.ToRegistration
                }
            }

            AuthorizationContract.Event.SignIn -> {
                setEffect {
                    AuthorizationContract.Effect.Navigation.ToAuthorization
                }
            }

            AuthorizationContract.Event.SignInWithGoogle -> {
                signInWithGoogle()
            }
        }
    }

    private fun signInWithGoogle() {
        /* TODO ADD GOOGLE  */
    }
}
