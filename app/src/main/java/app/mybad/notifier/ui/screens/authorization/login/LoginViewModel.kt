package app.mybad.notifier.ui.screens.authorization.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.authorization.LoginWithEmailUseCase
import app.mybad.domain.usecases.user.CreateUserUseCase
import app.mybad.domain.usecases.user.GetUserIdUseCase
import app.mybad.domain.usecases.user.UpdateUserAuthTokenUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.notifier.utils.isValidEmail
import app.mybad.notifier.utils.isValidPassword
import app.mybad.utils.toLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val updateUserAuthTokenUseCase: UpdateUserAuthTokenUseCase,
) : BaseViewModel<LoginContract.Event, LoginContract.State, LoginContract.Effect>() {
    init {
        log("init")
    }
    override fun setInitialState() = LoginContract.State()

    override fun handleEvents(event: LoginContract.Event) {
        when (event) {
            LoginContract.Event.ActionBack -> setEffect { LoginContract.Effect.Navigation.Back }

            LoginContract.Event.ForgotPassword -> setEffect { LoginContract.Effect.Navigation.ToForgotPassword }

            is LoginContract.Event.SignIn -> signIn(login = event.email, password = event.password)

            LoginContract.Event.SignInWithGoogle -> signInWithGoogle()

            is LoginContract.Event.UpdateLogin -> setState {
                copy(
                    email = event.newLogin,
                    isLoginButtonEnabled = password.isNotBlank() && event.newLogin.isNotBlank()
                )
            }

            is LoginContract.Event.UpdatePassword -> setState {
                copy(
                    password = event.newPassword,
                    isLoginButtonEnabled = event.newPassword.isNotBlank() && email.isNotBlank()
                )
            }
        }
    }

    private fun signIn(login: String, password: String) {
        viewModelScope.launch {
            AuthToken.clear()
            // проверка почты и пароля на валидность
            log("isValidParams")
            if (!isValidParams(login, password)) return@launch

            loginWithEmailUseCase(login = login, password = password)
                .onSuccess { result ->
                    // тут не только получение id, но и если его нет, то создается
                    val userId: Long = getUserIdUseCase(email = login) ?: createUserUseCase(
                        email = login,
                        name = "",
                    )
                    log("Ok: userId=$userId token=${
                            result.token
                        } date=${result.tokenDate} exp=${result.tokenDate.toLocalDateTime()}"
                    )
                    updateUserAuthTokenUseCase(
                        userId = userId,
                        token = result.token,
                        tokenDate = result.tokenDate,
                        tokenRefresh = result.tokenRefresh,
                        tokenRefreshDate = result.tokenRefreshDate,
                    )
                    setState { copy(isError = false, isLoading = false) }
                    setEffect { LoginContract.Effect.Navigation.ToMain }
                }
                .onFailure { error ->
                    log("Error", error)
                    setState { copy(isError = true, isLoading = false, isLoginButtonEnabled = false) }
                    //TODO("отобразить всплывающую ошибку")
                    val errorMessage =
                        error.message ?: error.localizedMessage ?: "Error: Authorization"
                }
        }
    }

    private fun isValidParams(login: String, password: String): Boolean {
        val isValidEmail = login.isValidEmail()
        log("isValidEmail=${isValidEmail}")
        val isValidPassword = password.isValidPassword()
        log("isValidPassword=${isValidPassword}")
        setState {
            copy(
                isError = !(isValidEmail && isValidPassword),
                isErrorEmail = !isValidEmail,
                isErrorPassword = !isValidPassword,
                isLoading = false,
                isLoginButtonEnabled = false,
            )
        }
        return isValidEmail && isValidPassword
    }

    private fun signInWithGoogle() {
    }

    private fun log(message: String, tr: Throwable? = null) {
        Log.w("VTTAG", "LoginViewModel::signIn: $message", tr)
    }

}