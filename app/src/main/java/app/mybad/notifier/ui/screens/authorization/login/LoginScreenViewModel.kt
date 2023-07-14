package app.mybad.notifier.ui.screens.authorization.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.CreateUserUseCase
import app.mybad.domain.usecases.DataStoreUseCase
import app.mybad.domain.usecases.GetUserIdUseCase
import app.mybad.domain.usecases.authorization.LoginWithEmailUseCase
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
class LoginScreenViewModel @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
) : ViewModel() {

    private val _event: Channel<LoginScreenEvents> = Channel()
    val event = _event.receiveAsFlow()

    fun signIn(login: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreUseCase.clear()
            // проверка почты на валидность
            if (!login.isValidEmail() || !password.isValidPassword()) {
                Log.w(
                    "VTTAG",
                    "AuthorizationScreenViewModel::logIn: Error: email or password is not valid!"
                )
                _event.send(LoginScreenEvents.InvalidCredentials)
                return@launch
            }
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
                    _event.send(LoginScreenEvents.LoginSuccessful)
                }

                is ApiResult.ApiError -> {
//                    _uiEvent.emit("${result.code} ${result.message}")
                }

                is ApiResult.ApiException -> {
//                    _uiEvent.emit("${result.e}")
                }
            }
        }
    }

    fun signInWithGoogle() {
    }
}