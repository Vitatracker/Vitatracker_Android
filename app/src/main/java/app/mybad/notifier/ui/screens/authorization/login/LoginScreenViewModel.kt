package app.mybad.notifier.ui.screens.authorization.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.CreateUserUseCase
import app.mybad.domain.usecases.DataStoreUseCase
import app.mybad.domain.usecases.GetUserIdUseCase
import app.mybad.domain.usecases.authorization.LoginWithEmailUseCase
import app.mybad.domain.utils.ApiResult
import app.mybad.network.models.response.Authorization
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.notifier.utils.isValidEmail
import app.mybad.notifier.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
) : BaseViewModel<LoginScreenContract.Event, LoginScreenContract.State, LoginScreenContract.Effect>() {
    override fun setInitialState(): LoginScreenContract.State {
        return LoginScreenContract.State(
            email = "",
            password = "",
            isLoggingByEmail = false,
            isLoggingByGoogle = false,
            isError = false
        )
    }

    override fun handleEvents(event: LoginScreenContract.Event) {
        when (event) {
            LoginScreenContract.Event.ActionBack -> setEffect { LoginScreenContract.Effect.Navigation.Back }
            LoginScreenContract.Event.ForgotPassword -> setEffect { LoginScreenContract.Effect.Navigation.ToForgotPassword }
            is LoginScreenContract.Event.LoginWithEmail -> signIn(login = event.email, password = event.password)
            LoginScreenContract.Event.LoginWithGoogle -> signInWithGoogle()
            is LoginScreenContract.Event.UpdateLogin -> setState { copy(email = event.newLogin) }
            is LoginScreenContract.Event.UpdatePassword -> setState { copy(password = event.newPassword) }
        }
    }

    private fun signIn(login: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreUseCase.clear()
            // проверка почты на валидность
            if (!login.isValidEmail() || !password.isValidPassword()) {
                Log.w(
                    "VTTAG",
                    "AuthorizationScreenViewModel::logIn: Error: email or password is not valid!"
                )
//                _event.send(LoginScreenEvents.InvalidCredentials)
                setState { copy(isError = true, isLoggingByEmail = false) }
                return@launch
            }
            setState { copy(isError = false, isLoggingByEmail = true) }
            when (
                val result = loginWithEmailUseCase(login = login, password = password)
            ) {
                is ApiResult.ApiSuccess -> {
                    val userId: Long = getUserIdUseCase(email = login) ?: createUserUseCase(
                        email = login,
                        name = ""
                    )
                    Log.w("VTTAG", "AuthorizationScreenViewModel::logIn: Ok: userId=$userId")
                    val tokens = result.data as Authorization
                    AuthToken.userId = userId
                    dataStoreUseCase.updateAll(tokens.token, tokens.refreshToken, userId, login)
                    setState { copy(isError = false, isLoggingByEmail = false) }
                    setEffect { LoginScreenContract.Effect.Navigation.ToMain }
                }

                is ApiResult.ApiError -> {
                    setState { copy(isError = true, isLoggingByEmail = false) }
//                    _uiEvent.emit("${result.code} ${result.message}")
                }

                is ApiResult.ApiException -> {
                    setState { copy(isError = true, isLoggingByEmail = false) }
//                    _uiEvent.emit("${result.e}")
                }
            }
        }
    }

    fun signInWithGoogle() {
    }
}