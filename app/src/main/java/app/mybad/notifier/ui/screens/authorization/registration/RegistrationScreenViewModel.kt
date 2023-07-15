package app.mybad.notifier.ui.screens.authorization.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.CreateUserUseCase
import app.mybad.domain.usecases.DataStoreUseCase
import app.mybad.domain.usecases.authorization.RegistrationUserUseCase
import app.mybad.domain.utils.ApiResult
import app.mybad.network.models.response.Authorization
import app.mybad.notifier.utils.isValidEmail
import app.mybad.notifier.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val registrationUserUseCase: RegistrationUserUseCase,
) : ViewModel() {

    private val _event: Channel<RegistrationScreenEvents> = Channel()
    val event = _event.receiveAsFlow()

    fun registration(login: String, password: String, userName: String, confirmPassword: String) {
        if (password != confirmPassword) {
            viewModelScope.launch {
                _event.send(RegistrationScreenEvents.PasswordsMismatch)
            }
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreUseCase.clear()
            if (!login.isValidEmail() || !password.isValidPassword()) {
                log("Error: email or password is not valid!")
                _event.send(RegistrationScreenEvents.InvalidCredentials)
//                _uiEvent.emit("Error: email or password is not valid!")
                return@launch
            }
            when (val result = registrationUserUseCase(login, password, userName)) {
                is ApiResult.ApiSuccess -> {
                    val userId: Long = createUserUseCase(email = login, name = userName)
                    val tokens = result.data as Authorization
                    AuthToken.userId = userId
                    AuthToken.token = tokens.token
                    log("Ok: userId=$userId token=${tokens.token}")
                    dataStoreUseCase.updateAll(tokens.token, tokens.refreshToken, userId, login)
                    _event.send(RegistrationScreenEvents.RegistrationSuccessful)
                }

                is ApiResult.ApiError -> {
                    log("ApiError ${result.code} ${result.message}")
                }

                is ApiResult.ApiException -> {
                    log("ApiException")
                }
            }
        }
    }

    private fun log(message: String) {
        Log.w("VTTAG", "AuthorizationScreenViewModel::registration: $message")
    }

    fun signInWithGoogle() {
        // TODO("Not yet implemented")
    }
}