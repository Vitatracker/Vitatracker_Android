package app.mybad.notifier.ui.screens.authorization

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.CreateUserUseCase
import app.mybad.domain.usecases.DataStoreUseCase
import app.mybad.domain.usecases.GetUserIdUseCase
import app.mybad.domain.usecases.authorization.LoginWithEmailUseCase
import app.mybad.domain.usecases.authorization.RegistrationUserUseCase
import app.mybad.domain.utils.ApiResult
import app.mybad.network.models.response.Authorization
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationScreenViewModel @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val registrationUserUseCase: RegistrationUserUseCase,
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun logIn(login: String, password: String) {
        viewModelScope.launch {
            dataStoreUseCase.clear()
            // проверка почты на валидность
            if (!isEmailValid(email = login) || !isPasswordValid(password)) {
                Log.w(
                    "VTTAG",
                    "AuthorizationScreenViewModel::logIn: Error: email or password is not valid!"
                )
                _uiEvent.emit("Error: email or password is not valid!")
                return@launch
            }
            when (
                val result = loginWithEmailUseCase(login = login, password = password)
            ) {
                is ApiResult.ApiSuccess -> {
                    // тут не только получение id, но и если его нет, то создается
                    val userId: Long = getUserIdUseCase(email = login) ?: createUserUseCase(
                        email = login,
                        name = ""
                    )
                    Log.w("VTTAG", "AuthorizationScreenViewModel::logIn: Ok: userId=$userId")
                    val tokens = result.data as Authorization
                    updateDaraStore(
                        token = tokens.token,
                        refreshToken = tokens.refreshToken,
                        userId = userId,
                        email = login,
                    )
                }

                is ApiResult.ApiError -> {
                    _uiEvent.emit("${result.code} ${result.message}")
                }

                is ApiResult.ApiException -> {
                    _uiEvent.emit("${result.e}")
                }
            }
        }
    }

    fun registration(login: String, password: String, userName: String) {
        viewModelScope.launch {
            dataStoreUseCase.clear()
            // проверка почты на валидность
            if (!isEmailValid(email = login) || !isPasswordValid(password)) {
                Log.w(
                    "VTTAG",
                    "AuthorizationScreenViewModel::registration: Error: email or password is not valid!"
                )
                _uiEvent.emit("Error: email or password is not valid!")
                return@launch
            }
            when (
                val result = registrationUserUseCase(
                    login = login,
                    password = password,
                    userName = userName
                )
            ) {
                is ApiResult.ApiSuccess -> {
                    // добавить в локальную db user и получим userId
                    val userId: Long = createUserUseCase(email = login, name = userName)
                    Log.w("VTTAG", "AuthorizationScreenViewModel::registration: Ok: userId=$userId")
                    val tokens = result.data as Authorization
                    updateDaraStore(
                        token = tokens.token,
                        refreshToken = tokens.refreshToken,
                        userId = userId,
                        email = login,
                    )
                }

                is ApiResult.ApiError -> {
                    Log.w("VTTAG", "AuthorizationScreenViewModel::registration: ApiError")
                    _uiEvent.emit("${result.code} ${result.message}")
                }

                is ApiResult.ApiException -> {
                    Log.w("VTTAG", "AuthorizationScreenViewModel::registration: ApiException")
                    _uiEvent.emit("${result.e}")
                }
            }
        }
    }

    fun loginWithFacebook() {
//        viewModelScope.launch { authorizationRepo.loginWithFacebook() }
    }

    fun loginWithGoogle() {
//        viewModelScope.launch { authorizationRepo.loginWithGoogle() }
    }

    private fun isEmailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isPasswordValid(password: String) = password.length > 3

    private suspend fun updateDaraStore(
        token: String,
        refreshToken: String,
        userId: Long,
        email: String
    ) {
        Log.w("VTTAG", "AuthorizationScreenViewModel::updateDaraStore: userId=$userId email=$email")
        dataStoreUseCase.updateToken(token)
        dataStoreUseCase.updateRefreshToken(refreshToken)
        dataStoreUseCase.updateEmail(email)
        // получить userId из локальной базы
        dataStoreUseCase.updateUserId(userId)
    }

}
