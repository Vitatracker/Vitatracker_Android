package app.mybad.notifier.ui.screens.authorization.registration

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.authorization.RegistrationUserUseCase
import app.mybad.domain.usecases.user.CreateUserUseCase
import app.mybad.domain.usecases.user.UpdateUserAuthTokenUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.notifier.utils.isValidEmail
import app.mybad.notifier.utils.isValidPassword
import app.mybad.utils.toLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationUserUseCase: RegistrationUserUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val updateUserAuthTokenUseCase: UpdateUserAuthTokenUseCase,
) :
    BaseViewModel<RegistrationContract.Event, RegistrationContract.State, RegistrationContract.Effect>() {

    override fun setInitialState() = RegistrationContract.State()

    override fun handleEvents(event: RegistrationContract.Event) {
        when (event) {
            RegistrationContract.Event.ActionBack -> setEffect { RegistrationContract.Effect.Navigation.Back }
            is RegistrationContract.Event.CreateAccount -> {
                registration(event.email, event.password, event.confirmationPassword)
            }

            RegistrationContract.Event.SignInWithGoogle -> signInWithGoogle()
            is RegistrationContract.Event.UpdateConfirmationPassword -> setState {
                copy(
                    confirmationPassword = event.newConfirmationPassword,
                    error = null,
                )
            }

            is RegistrationContract.Event.UpdateEmail -> setState {
                copy(
                    email = event.newEmail,
                    error = null,
                    isRegistrationButtonEnabled = email.isNotBlank() && password.isNotBlank()
                )
            }

            is RegistrationContract.Event.UpdatePassword -> setState {
                copy(
                    password = event.newPassword,
                    error = null,
                    isRegistrationButtonEnabled = email.isNotBlank() && password.isNotBlank()
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
            setState { copy(error = null, isLoading = true) }
            // проверка почты на валидность
            if (!isValidParams(login, password, confirmPassword)) return@launch
            AuthToken.clear()
            registrationUserUseCase(
                login = login,
                password = password,
            ).onSuccess { result ->
                // добавить в локальную db user и получим userId
                val userId: Long = createUserUseCase(email = login)
                log(
                    "Ok: userId=$userId token=${result.token} date=${
                        result.tokenDate
                    } exp=${result.tokenDate.toLocalDateTime()}"
                )
                updateUserAuthTokenUseCase(
                    userId = userId,
                    token = result.token,
                    tokenDate = result.tokenDate,
                    tokenRefresh = result.tokenRefresh,
                    tokenRefreshDate = result.tokenRefreshDate,
                )
                setState { copy(isLoading = false) }
                setEffect { RegistrationContract.Effect.Navigation.ToMain }
            }.onFailure { error ->
                log("Error", error)
//                error.message ?: error.localizedMessage ?: "Error: Registration"
                (error.message ?: error.localizedMessage)?.let(::checkErrorMessage)
            }
        }
    }

    private fun isValidParams(login: String, password: String, confirmPassword: String): Boolean {
        if (password != confirmPassword) {
            setState {
                copy(
                    error = RegistrationContract.RegistrationError.PasswordsMismatch,
                    isLoading = false
                )
            }
            log("Error: passwords mismatch")
            return false
        }
        if (!login.isValidEmail()) {
            log("Error: email is not valid!")
            setState {
                copy(
                    error = RegistrationContract.RegistrationError.WrongEmailFormat,
                    isLoading = false
                )
            }
            return false
        }
        if (!password.isValidPassword()) {
            log("Error: password is not valid!")
            setState {
                copy(
                    error = RegistrationContract.RegistrationError.WrongPassword,
                    isLoading = false
                )
            }
            return false
        }
        return true
    }

    private fun checkErrorMessage(message: String) {
        setState {
            copy(
                error = if (message.contains("email", ignoreCase = true)) {
                    RegistrationContract.RegistrationError.WrongEmailFormat
                } else RegistrationContract.RegistrationError.WrongPassword,
                isLoading = false,
            )
        }
    }

    private fun log(message: String, tr: Throwable? = null) {
        Log.w("VTTAG", "RegistrationViewModel::registration: $message", tr)
    }

    private fun signInWithGoogle() {
    }
}
