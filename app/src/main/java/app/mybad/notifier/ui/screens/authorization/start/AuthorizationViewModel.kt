package app.mybad.notifier.ui.screens.authorization.start

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.authorization.GetLoginGoogleIntentUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.notifier.ui.screens.authorization.registration.RegistrationContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import net.openid.appauth.TokenRequest
import javax.inject.Inject

@HiltViewModel
class AuthorizationViewModel @Inject constructor(
    private val getLoginGoogleIntentUseCase: GetLoginGoogleIntentUseCase,
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

            AuthorizationContract.Event.OpenGoogleLoginPage -> openGoogleLoginPage()
            is AuthorizationContract.Event.TokenExchange -> getToken(event.tokenRequest)
            AuthorizationContract.Event.SignInWithGoogle -> signInWithGoogle()
        }
    }

    private fun openGoogleLoginPage() {
        viewModelScope.launch {
            val intent = getLoginGoogleIntentUseCase()
            setEffect { AuthorizationContract.Effect.OpenAuthPage(intent) }
        }
    }

    private fun getToken(token: TokenRequest) {
        /* TODO ADD GOOGLE  */
        log("token=${token}")
    }

    private fun signInWithGoogle() {
        /* TODO ADD GOOGLE  */
    }

    private fun log(message: String, tr: Throwable? = null) {
        if (tr == null) Log.w("VTTAG", "AuthorizationViewModel::authorization: $message")
        else Log.e("VTTAG", "AuthorizationViewModel::authorization: $message", tr)
    }

}
