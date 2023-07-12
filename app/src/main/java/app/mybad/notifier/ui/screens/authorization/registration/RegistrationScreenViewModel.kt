package app.mybad.notifier.ui.screens.authorization.registration

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.CreateUserUseCase
import app.mybad.domain.usecases.DataStoreUseCase
import app.mybad.domain.usecases.authorization.RegistrationUserUseCase
import app.mybad.domain.utils.ApiResult
import app.mybad.network.models.response.Authorization
import app.mybad.notifier.utils.isValidEmail
import app.mybad.notifier.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val registrationUserUseCase: RegistrationUserUseCase,
) : ViewModel() {

    fun registration(login: String, password: String, userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreUseCase.clear()
            // проверка почты на валидность
            if (!login.isValidEmail() || !password.isValidPassword()) {
                Log.w(
                    "VTTAG",
                    "AuthorizationScreenViewModel::registration: Error: email or password is not valid!"
                )
//                _uiEvent.emit("Error: email or password is not valid!")
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
                    val tokens = result.data as Authorization
                    Log.w("VTTAG", "AuthorizationScreenViewModel::registration: Ok: userId=$userId token=${tokens.token}")
                    dataStoreUseCase.updateAll(tokens.token, tokens.refreshToken, userId, login)
                }

                is ApiResult.ApiError -> {
                    Log.w("VTTAG", "AuthorizationScreenViewModel::registration: ApiError ${result.code} ${result.message}")
//                    _uiEvent.emit("${result.code} ${result.message}")
                }

                is ApiResult.ApiException -> {
                    Log.w("VTTAG", "AuthorizationScreenViewModel::registration: ApiException")
//                    _uiEvent.emit("${result.e}")
                }
            }
        }
    }
}