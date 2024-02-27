package app.mybad.notifier.ui.screens.authorization.firebase.sign_in

import android.content.Intent
import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.authorization.GetFirebaseGoogleAuthIntentUseCase
import app.mybad.domain.usecases.authorization.SignInFirebaseGoogleAuthUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseSignInViewModel @Inject constructor(
    private val getFirebaseGoogleAuthIntentUseCase: GetFirebaseGoogleAuthIntentUseCase,
    private val signInFirebaseGoogleAuthUseCase: SignInFirebaseGoogleAuthUseCase,
) : BaseViewModel<FirebaseSignInContract.Event, FirebaseSignInContract.State, FirebaseSignInContract.Effect>() {

    override fun setInitialState() = FirebaseSignInContract.State()

    override fun handleEvents(event: FirebaseSignInContract.Event) {
        when (event) {
            FirebaseSignInContract.Event.ActionBack -> setEffect { FirebaseSignInContract.Effect.Navigation.Back }

            is FirebaseSignInContract.Event.SignIn -> {}//signIn(login = event.email, password = event.password)

            FirebaseSignInContract.Event.OpenGoogleLoginPage -> openGoogleLoginPage()
            is FirebaseSignInContract.Event.SignInWithGoogle -> signInWithGoogle(event.intent)
        }
    }

    private fun signInWithGoogle(intent: Intent) {
        viewModelScope.launch {
            Log.w(
                "VTTAG",
                "FirebaseSignInViewModel::signInWithGoogle: in"
            )
            signInFirebaseGoogleAuthUseCase(intent).onSuccess { user ->
                if (user != null && user.userEmail.isNotBlank()) {
                    Log.w(
                        "VTTAG",
                        "FirebaseSignInViewModel::signInWithGoogle: user=${user.userEmail}"
                    )
                    // проверить есть ли на сервере такой пользователь

                    // если нет пользователя зарегистрировать

                    // обработать получение токена
                }
            }
        }
    }

    private fun openGoogleLoginPage() {
        viewModelScope.launch {
            getFirebaseGoogleAuthIntentUseCase().onSuccess { intent ->
                setEffect { FirebaseSignInContract.Effect.OpenAuthPage(intent) }
            }
        }
    }

}
