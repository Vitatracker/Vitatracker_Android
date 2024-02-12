package app.mybad.notifier.ui.screens.authorization.registration

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.authorization.GetLoginGoogleIntentUseCase
import app.mybad.domain.usecases.authorization.RegistrationUserUseCase
import app.mybad.domain.usecases.user.CreateUserUseCase
import app.mybad.domain.usecases.user.GetUserIdUseCase
import app.mybad.domain.usecases.user.UpdateUserAuthTokenUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.notifier.ui.screens.authorization.login.LoginContract
import app.mybad.notifier.utils.isValidEmail
import app.mybad.notifier.utils.isValidPassword
import app.mybad.utils.toDateTimeUTC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationUserUseCase: RegistrationUserUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val updateUserAuthTokenUseCase: UpdateUserAuthTokenUseCase,
    private val getLoginGoogleIntentUseCase: GetLoginGoogleIntentUseCase,
) :
    BaseViewModel<RegistrationContract.Event, RegistrationContract.State, RegistrationContract.Effect>() {

    override fun setInitialState() = RegistrationContract.State()

    override fun handleEvents(event: RegistrationContract.Event) {
        when (event) {
            RegistrationContract.Event.OnBack -> setEffect { RegistrationContract.Effect.Navigation.Back }

            is RegistrationContract.Event.CreateAccount -> {
                registration(event.email, event.password, event.confirmationPassword)
            }

            RegistrationContract.Event.SignInWithGoogle -> signInWithGoogle()

            RegistrationContract.Event.OpenGoogleLoginPage -> openGoogleLoginPage()

            is RegistrationContract.Event.TokenExchange -> TODO()

            is RegistrationContract.Event.UpdateEmail -> {
                checkParams(
                    email = event.newEmail,
                    password = viewState.value.password,
                    confirmationPassword = viewState.value.confirmationPassword,
                )
            }

            is RegistrationContract.Event.UpdatePassword -> {
                checkParams(
                    email = viewState.value.email,
                    password = event.newPassword,
                    confirmationPassword = viewState.value.confirmationPassword,
                )
            }

            is RegistrationContract.Event.UpdateConfirmationPassword -> {
                checkParams(
                    email = viewState.value.email,
                    password = viewState.value.password,
                    confirmationPassword = event.newConfirmationPassword,
                )
            }

            RegistrationContract.Event.ShowUserAgreement -> {
                //TODO("add user agreement navigation")
            }

        }
    }

    fun registration(login: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            log("start")
            // для обновления состояния необходимо запускать в launch
            launch {
                setState {
                    copy(
                        error = null,
                        isErrorEmail = false,
                        isErrorPassword = false,
                        isLoading = true,
                        isRegistrationButtonEnabled = false,
                    )
                }
            }
            // проверка почты на валидность
            if (!isValidParams(login, password, confirmPassword)) return@launch
            registrationUserUseCase(
                login = login,
                password = password,
            ).onSuccess { result ->
                if (result.message.isBlank()) {
                    // добавить в локальную db user и получим userId
                    val userId: Long = getUserIdUseCase(email = login) ?: createUserUseCase(
                        email = login,
                        name = "",
                    )
                    log(
                        "Ok: userId=$userId token=${result.token} date=${
                            result.tokenDate
                        } exp=${result.tokenDate.toDateTimeUTC()}"
                    )
                    updateUserAuthTokenUseCase(
                        userId = userId,
                        token = result.token,
                        tokenDate = result.tokenDate,
                        tokenRefresh = result.tokenRefresh,
                        tokenRefreshDate = result.tokenRefreshDate,
                    )
                    setEffect { RegistrationContract.Effect.Navigation.ToMain }
                } else checkErrorMessage(result.message)
            }.onFailure { error ->
                log("Error", error)
                //TODO(" сделать отображение ошибки возможно такой пользователь уже существует или не правильная почта или пароль")
                (error.message ?: error.localizedMessage)?.let(::checkErrorMessage)
            }
        }
    }

    private fun isValidParams(
        login: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var isErrorEmail = false
        var isErrorPassword = false
        val registrationError = when {
            password != confirmPassword -> {
                log("Error: passwords mismatch")
                RegistrationContract.RegistrationError.PasswordsMismatch
            }

            !login.isValidEmail() -> {
                isErrorEmail = true
                log("Error: email is not valid!")
                RegistrationContract.RegistrationError.WrongEmailFormat
            }

            !password.isValidPassword() -> {
                isErrorPassword = true
                log("Error: password is not valid!")
                RegistrationContract.RegistrationError.WrongPassword
            }

            else -> null
        }
        registrationError?.let { error ->
            viewModelScope.launch {
                setState {
                    copy(
                        error = error,
                        isErrorEmail = isErrorEmail,
                        isErrorPassword = isErrorPassword,
                        isLoading = false
                    )
                }
            }
        }
        return registrationError == null
    }

    private fun checkErrorMessage(message: String) {
        viewModelScope.launch {
            log("checkErrorMessage message=$message")
            var isErrorEmail = false
            var isErrorPassword = false
            val error = when {
                message.contains("user", ignoreCase = true) &&
                    message.contains("already exists", ignoreCase = true) -> {
                    log("checkErrorMessage isErrorEmail = true")
                    isErrorEmail = true
                    RegistrationContract.RegistrationError.UserEmailExists
                }

                message.contains("email", ignoreCase = true) -> {
                    log("checkErrorMessage isErrorEmail = true")
                    isErrorEmail = true
                    RegistrationContract.RegistrationError.WrongEmailFormat
                }

                message.contains("password", ignoreCase = true) -> {
                    log("checkErrorMessage isErrorPassword = true")
                    isErrorPassword = true
                    RegistrationContract.RegistrationError.WrongPassword
                }

                else -> {
                    //TODO("тут нужно вывести тост")
                    RegistrationContract.RegistrationError.Error(message)
                }
            }
            setState {
                copy(
                    error = error,
                    isErrorEmail = isErrorEmail,
                    isErrorPassword = isErrorPassword,
                    isLoading = false,
                )
            }
        }
    }

    private fun checkParams(
        email: String,
        password: String,
        confirmationPassword: String,
    ) {
        viewModelScope.launch {
            setState {
                copy(
                    error = null,
                    isErrorEmail = password.isNotBlank() && email.isNotBlank() &&
                        !(email.contains("@") && email.contains(".")),
                    isErrorPassword = false,
                    email = email,
                    password = password,
                    confirmationPassword = confirmationPassword,
                    isRegistrationButtonEnabled = email.isNotBlank() && password.isNotBlank() && confirmationPassword.isNotBlank()
                        && email.contains("@") && email.contains(".")
                )
            }
        }
    }

    private fun log(message: String, tr: Throwable? = null) {
        if (tr == null) Log.w("VTTAG", "RegistrationViewModel::registration: $message")
        else Log.e("VTTAG", "RegistrationViewModel::registration: $message", tr)
    }

    private fun signInWithGoogle() {
    }

    private fun openGoogleLoginPage() {
        viewModelScope.launch {
            val intent = getLoginGoogleIntentUseCase()
            setEffect { RegistrationContract.Effect.OpenAuthPage(intent) }
        }
    }

}
