package app.mybad.notifier.ui.screens.authorization.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.CreateUserUseCase
import app.mybad.domain.usecases.DataStoreUseCase
import app.mybad.domain.usecases.GetUserIdUseCase
import app.mybad.domain.usecases.authorization.LoginWithEmailUseCase
import app.mybad.domain.utils.ApiResult
import app.mybad.network.models.response.Authorization
import app.mybad.notifier.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
) : BaseViewModel<LoginScreenContract.Event, LoginScreenContract.State, LoginScreenContract.Effect>() {
    override fun setInitialState(): LoginScreenContract.State {
        return LoginScreenContract.State(
            email = "",
            password = "",
            isLoading = false,
            isError = false,
            isLoginEnabled = false
        )
    }

    override fun handleEvents(event: LoginScreenContract.Event) {
        when (event) {
            LoginScreenContract.Event.ActionBack -> setEffect { LoginScreenContract.Effect.Navigation.Back }
            LoginScreenContract.Event.ForgotPassword -> setEffect { LoginScreenContract.Effect.Navigation.ToForgotPassword }
            is LoginScreenContract.Event.SignIn -> signIn(login = event.email, password = event.password)
            LoginScreenContract.Event.SignInWithGoogle -> signInWithGoogle()
            is LoginScreenContract.Event.UpdateLogin -> setState {
                copy(
                    email = event.newLogin,
                    isLoginEnabled = password.isNotBlank() && event.newLogin.isNotBlank()
                )
            }

            is LoginScreenContract.Event.UpdatePassword -> setState {
                copy(
                    password = event.newPassword,
                    isLoginEnabled = event.newPassword.isNotBlank() && email.isNotBlank()
                )
            }
        }
    }

    private fun signIn(login: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreUseCase.clear()
            // проверка почты на валидность
            if (login.isEmpty() || password.isEmpty()) {
                setState {
                    copy(
                        isError = true,
                        isLoading = false,
                        isLoginEnabled = false
                    )
                }
                return@launch
            }
            setState { copy(isError = false, isLoading = true, isLoginEnabled = false) }
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
                    setState { copy(isError = false, isLoading = false) }
                    setEffect { LoginScreenContract.Effect.Navigation.ToMain }
                }

                is ApiResult.ApiError -> {
                    setState { copy(isError = true, isLoading = false, isLoginEnabled = true) }
                }

                is ApiResult.ApiException -> {
                    setState { copy(isError = true, isLoading = false, isLoginEnabled = true) }
                }
            }
        }
    }

    private fun signInWithGoogle() {
    }
}