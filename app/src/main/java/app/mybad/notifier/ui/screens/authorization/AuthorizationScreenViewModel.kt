package app.mybad.notifier.ui.screens.authorization

import androidx.lifecycle.ViewModel
import app.mybad.domain.repos.AuthorizationRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthorizationScreenViewModel @Inject constructor(
    private val authorizationRepo: AuthorizationRepo
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _uiState = MutableStateFlow(AuthorizationScreenContract())
    val uiState = _uiState.asStateFlow()

    suspend fun logIn(login: String, password: String) {
        val result = authorizationRepo.loginWithEmail(login = login, password = password)
    }

    fun registration(login: String, password: String, userName: String) {
        scope.launch {
            authorizationRepo.registrationUser(
                login = login,
                password = password,
                userName = userName
            )
        }
    }

    fun loginWithFacebook() {
        scope.launch { authorizationRepo.loginWithFacebook() }
    }

    fun loginWithGoogle() {
        scope.launch { authorizationRepo.loginWithGoogle() }
    }

}
