package app.mybad.notifier.ui.screens.authorization.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.authorization.LoginWithEmailUseCase
import app.mybad.domain.usecases.user.CreateUserUseCase
import app.mybad.domain.usecases.user.GetUserIdUseCase
import app.mybad.domain.usecases.user.UpdateUserAuthTokenUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.notifier.utils.isValidEmail
import app.mybad.notifier.utils.isValidPassword
import app.mybad.utils.toDateTimeUTC
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

            is LoginContract.Event.UpdateLogin -> {
                checkParams(email = event.newLogin, password = viewState.value.password)
            }

            is LoginContract.Event.UpdatePassword -> {
                checkParams(email = viewState.value.email, password = event.newPassword)
            }
        }
    }

    private fun signIn(login: String, password: String) {
        viewModelScope.launch {
            // тут обязательно в launch
            launch {
                setState {
                    copy(
                        isError = false,
                        isErrorEmail = false,
                        isErrorPassword = false,

                        isLoading = true,
                        isLoginButtonEnabled = false,
                    )
                }
            }
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
                    log(
                        "Ok: userId=$userId token=${
                            result.token
                        } date=${result.tokenDate} exp=${result.tokenDate.toDateTimeUTC()}"
                    )
                    updateUserAuthTokenUseCase(
                        userId = userId,
                        token = result.token,
                        tokenDate = result.tokenDate,
                        tokenRefresh = result.tokenRefresh,
                        tokenRefreshDate = result.tokenRefreshDate,
                    )
                    setEffect { LoginContract.Effect.Navigation.ToMain }
                }
                .onFailure { error ->
                    log("Error", error)
                    //TODO("отобразить всплывающую ошибку")
                    val errorMessage =
                        error.message ?: error.localizedMessage ?: "Error: Authorization"
                    setState {
                        copy(
                            isError = true,
                            isErrorEmail = true,
                            isErrorPassword = true,
                            isLoading = false,
                            isLoginButtonEnabled = true
                        )
                    }
                }
        }
    }

    private fun isValidParams(login: String, password: String): Boolean {
        val isValidEmail = login.isValidEmail()
        log("isValidEmail=${isValidEmail}")
        val isValidPassword = password.isValidPassword()
        log("isValidPassword=${isValidPassword}")
        if (!(isValidEmail && isValidPassword)) {
            viewModelScope.launch {
                setState {
                    copy(
                        isError = true,
                        isErrorEmail = !isValidEmail,
                        isErrorPassword = !isValidPassword,

                        isLoading = false,
                        isLoginButtonEnabled = false,
                    )
                }
            }
        }
        return isValidEmail && isValidPassword
    }

    private fun checkParams(
        email: String,
        password: String,
    ) {
        viewModelScope.launch {
            setState {
                copy(
                    isError = false,
                    isErrorEmail = false,
                    isErrorPassword = false,

                    email = email,
                    password = password,

                    isLoginButtonEnabled = email.isNotBlank() && password.isNotBlank()
                            && email.contains("@")
                )
            }
        }
    }

    private fun signInWithGoogle() {

    }

    private fun log(message: String, tr: Throwable? = null) {
        Log.w("VTTAG", "LoginViewModel::signIn: $message", tr)
    }

}
