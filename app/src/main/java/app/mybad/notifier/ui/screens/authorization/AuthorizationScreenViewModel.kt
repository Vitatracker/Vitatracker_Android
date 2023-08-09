package app.mybad.notifier.ui.screens.authorization

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.authorization.LoginWithEmailUseCase
import app.mybad.domain.usecases.authorization.LoginWithFacebookUseCase
import app.mybad.domain.usecases.authorization.LoginWithGoogleUseCase
import app.mybad.domain.usecases.authorization.RegistrationUserUseCase
import app.mybad.domain.usecases.user.CreateUserUseCase
import app.mybad.domain.usecases.user.GetUserIdUseCase
import app.mybad.domain.usecases.user.UpdateUserAuthTokenUseCase
import app.mybad.utils.toLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationScreenViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,

    private val registrationUserUseCase: RegistrationUserUseCase,
    private val loginWithEmailUseCase: LoginWithEmailUseCase,

    private val loginWithFacebookUseCase: LoginWithFacebookUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,

    private val updateUserAuthTokenUseCase: UpdateUserAuthTokenUseCase,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun logIn(login: String, password: String) {
        viewModelScope.launch {
            Log.w(
                "VTTAG", "AuthorizationScreenViewModel::logIn: start"
            )
            AuthToken.clear()
            // проверка почты на валидность
            if (!isEmailValid(email = login) || !isPasswordValid(password)) {
                Log.w(
                    "VTTAG",
                    "AuthorizationScreenViewModel::logIn: Error: email or password is not valid!"
                )
                _uiEvent.emit("Error: email or password is not valid!")
                return@launch
            }
            loginWithEmailUseCase(login = login, password = password)
                .onSuccess { result ->
                    // тут не только получение id, но и если его нет, то создается
                    val userId: Long = getUserIdUseCase(email = login) ?: createUserUseCase(
                        email = login,
                        name = ""
                    )
                    Log.w(
                        "VTTAG",
                        "AuthorizationScreenViewModel::logIn: Ok: userId=$userId token=${result.token} date=${result.tokenDate} exp=${result.tokenDate.toLocalDateTime()}"
                    )
                    updateUserAuthTokenUseCase(
                        userId = userId,
                        token = result.token,
                        tokenDate = result.tokenDate,
                        tokenRefresh = result.tokenRefresh,
                        tokenRefreshDate = result.tokenRefreshDate,
                    )
                }
                .onFailure { error ->
                    Log.w(
                        "VTTAG",
                        "AuthorizationScreenViewModel::logIn: Error", error
                    )
                    error.message?.let { message ->
                        _uiEvent.emit(message)
                    } ?: error.localizedMessage?.let { message ->
                        _uiEvent.emit(message)
                    } ?: _uiEvent.emit("Error: Authorization")
                }
        }
    }

    fun registration(login: String, password: String, userName: String) {
        viewModelScope.launch {
            Log.w(
                "VTTAG", "AuthorizationScreenViewModel::registration: start"
            )
            AuthToken.clear()
            // проверка почты на валидность
            if (!isEmailValid(email = login) || !isPasswordValid(password)) {
                Log.w(
                    "VTTAG",
                    "AuthorizationScreenViewModel::registration: Error: email or password is not valid!"
                )
                _uiEvent.emit("Error: email or password is not valid!")
                return@launch
            }
            registrationUserUseCase(
                login = login,
                password = password,
                userName = userName,
            ).onSuccess { result ->
                // добавить в локальную db user и получим userId
                val userId: Long = createUserUseCase(email = login, name = userName)
                Log.w(
                    "VTTAG",
                    "AuthorizationScreenViewModel::registration: Ok: userId=$userId token=${result.token} date=${result.tokenDate} exp=${result.tokenDate.toLocalDateTime()}"
                )
                updateUserAuthTokenUseCase(
                    userId = userId,
                    token = result.token,
                    tokenDate = result.tokenDate,
                    tokenRefresh = result.tokenRefresh,
                    tokenRefreshDate = result.tokenRefreshDate,
                )
            }.onFailure { error ->
                Log.w("VTTAG", "AuthorizationScreenViewModel::registration: Error", error)
                error.message?.let { message ->
                    _uiEvent.emit(message)
                } ?: error.localizedMessage?.let { message ->
                    _uiEvent.emit(message)
                } ?: _uiEvent.emit("Error: Registration")
            }
        }
    }

    fun loginWithFacebook() {
        viewModelScope.launch {
            loginWithFacebookUseCase()
                .onSuccess { }
                .onFailure {
                    _uiEvent.emit("Error: login with Facebook not realized")
                }
        }
    }

    fun loginWithGoogle() {
        viewModelScope.launch {
            loginWithGoogleUseCase()
                .onSuccess { }
                .onFailure {
                    _uiEvent.emit("Error: login with Facebook not realized")
                }
        }

    }

    private fun isEmailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isPasswordValid(password: String) = password.length > 3

}
