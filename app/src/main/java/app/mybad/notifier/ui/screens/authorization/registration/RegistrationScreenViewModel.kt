package app.mybad.notifier.ui.screens.authorization.registration

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.authorization.RegistrationCreateAccountErrorDomainModel
import app.mybad.domain.usecases.CreateUserUseCase
import app.mybad.domain.usecases.DataStoreUseCase
import app.mybad.domain.usecases.authorization.RegistrationUserUseCase
import app.mybad.domain.utils.ApiResult
import app.mybad.network.models.response.Authorization
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.notifier.ui.screens.authorization.login.LoginScreenContract
import app.mybad.notifier.utils.isValidEmail
import app.mybad.notifier.utils.isValidPassword
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val registrationUserUseCase: RegistrationUserUseCase,
) : BaseViewModel<RegistrationScreenContract.Event, RegistrationScreenContract.State, RegistrationScreenContract.Effect>() {

    override fun setInitialState(): RegistrationScreenContract.State {
        return RegistrationScreenContract.State(
            email = "",
            password = "",
            confirmationPassword = "",
            isLoading = false,
            error = null,
            isRegistrationEnabled = false
        )
    }

    override fun handleEvents(event: RegistrationScreenContract.Event) {
        when (event) {
            RegistrationScreenContract.Event.ActionBack -> setEffect { RegistrationScreenContract.Effect.Navigation.Back }
            is RegistrationScreenContract.Event.CreateAccount -> {
                registration(event.email, event.password, event.confirmationPassword)
            }

            RegistrationScreenContract.Event.SignInWithGoogle -> signInWithGoogle()
            is RegistrationScreenContract.Event.UpdateConfirmationPassword -> setState {
                copy(
                    confirmationPassword = event.newConfirmationPassword,
                    error = null,
                )
            }

            is RegistrationScreenContract.Event.UpdateEmail -> setState {
                copy(
                    email = event.newEmail,
                    error = null,
                    isRegistrationEnabled = email.isNotBlank() && password.isNotBlank()
                )
            }

            is RegistrationScreenContract.Event.UpdatePassword -> setState {
                copy(
                    password = event.newPassword,
                    error = null,
                    isRegistrationEnabled = email.isNotBlank() && password.isNotBlank()
                )
            }

            RegistrationScreenContract.Event.ShowUserAgreement -> {
                //TODO("add user agreement navigation")
            }
        }
    }

    fun registration(login: String, password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            setState { copy(error = RegistrationScreenContract.RegistrationError.PasswordsMismatch, isLoading = false) }
            log("Error: passwords mismatch")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreUseCase.clear()
            if (!login.isValidEmail()) {
                log("Error: email is not valid!")
                setState { copy(error = RegistrationScreenContract.RegistrationError.WrongEmailFormat, isLoading = false) }
                return@launch
            }
            if (!password.isValidPassword()) {
                log("Error: password is not valid!")
                setState { copy(error = RegistrationScreenContract.RegistrationError.WrongPassword, isLoading = false) }
                return@launch
            }
            setState { copy(error = null, isLoading = true) }
            when (val result = registrationUserUseCase(login, password)) {
                is ApiResult.ApiSuccess -> {
                    val userId: Long = createUserUseCase(email = login)
                    val tokens = result.data as Authorization
                    AuthToken.userId = userId
                    AuthToken.token = tokens.token
                    log("Ok: userId=$userId token=${tokens.token}")
                    dataStoreUseCase.updateAll(tokens.token, tokens.refreshToken, userId, login)
                    setEffect { RegistrationScreenContract.Effect.Navigation.ToMain }
                    setState { copy(isLoading = false) }
                }

                is ApiResult.ApiError -> {
                    log("ApiError ${result.code} ${result.message}")
                    try {
                        val error = Gson().fromJson(result.message, RegistrationCreateAccountErrorDomainModel::class.java)!!
                        if (error.violations.isNotEmpty()) {
                            val invalidField = error.violations[0]
                            if (invalidField.fieldName == "email") {
                                setState { copy(error = RegistrationScreenContract.RegistrationError.WrongEmailFormat, isLoading = false) }
                            } else {
                                setState { copy(error = RegistrationScreenContract.RegistrationError.WrongPassword, isLoading = false) }
                            }
                        }
                    } catch (e: Exception) {
                        // ignore
                        setState { copy(error = RegistrationScreenContract.RegistrationError.WrongPassword, isLoading = false) }
                    }
                }

                is ApiResult.ApiException -> {
                    log("ApiException")
                    setState { copy(error = RegistrationScreenContract.RegistrationError.WrongPassword, isLoading = false) }
                }
            }
        }
    }

    private fun log(message: String) {
        Log.w("VTTAG", "AuthorizationScreenViewModel::registration: $message")
    }

    private fun signInWithGoogle() {
    }
}