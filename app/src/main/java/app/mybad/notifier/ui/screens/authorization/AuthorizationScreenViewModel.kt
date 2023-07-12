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

class AuthorizationScreenViewModel(
    private val dataStoreUseCase: DataStoreUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val registrationUserUseCase: RegistrationUserUseCase,
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

}
