package app.mybad.notifier.ui.screens.settings.profile

import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.user.ClearUserAuthTokenUseCase
import app.mybad.domain.usecases.user.DeleteUserAccountUseCase
import app.mybad.domain.usecases.user.GetUserPersonalUseCase
import app.mybad.domain.usecases.user.UpdateUserPersonalUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsProfileViewModel @Inject constructor(
    private val getUserPersonalUseCase: GetUserPersonalUseCase,
    private val updateUserPersonalUseCase: UpdateUserPersonalUseCase,
    private val clearUserAuthTokenUseCase: ClearUserAuthTokenUseCase,
    private val deleteUserAccountUseCase: DeleteUserAccountUseCase,
) : BaseViewModel<SettingsProfileContract.Event, SettingsProfileContract.State, SettingsProfileContract.Effect>() {

    init {
        setModelFromSaved()
    }

    private fun setModelFromSaved() {
        viewModelScope.launch {
            val model = getUserPersonalUseCase()
            setState {
                copy(
                    name = model.name ?: "",
                    email = model.email ?: "",
                    isLoading = false
                )
            }
        }
    }

    override fun setInitialState() = SettingsProfileContract.State()

    override fun handleEvents(event: SettingsProfileContract.Event) {
        when (event) {
            SettingsProfileContract.Event.ActionBack -> {
                setEffect { SettingsProfileContract.Effect.Navigation.Back }
            }

            SettingsProfileContract.Event.ChangePassword -> {
                setEffect { SettingsProfileContract.Effect.Navigation.ToChangePassword }
            }

            is SettingsProfileContract.Event.OnUserNameChanged -> {
                setState { copy(name = event.name) }
            }

            SettingsProfileContract.Event.DeleteAccount -> deleteAccount()
            SettingsProfileContract.Event.SignOut -> signOut()
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            val model = getUserPersonalUseCase()
            updateUserPersonalUseCase(model.copy(name = viewState.value.name))
            super.onCleared()
        }
    }

    private fun deleteAccount() {
//        TODO("Not yet implemented")
        viewModelScope.launch {
            if (deleteUserAccountUseCase()) {
                setEffect { SettingsProfileContract.Effect.Navigation.ToAuthorization }
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            clearUserAuthTokenUseCase()
            setEffect { SettingsProfileContract.Effect.Navigation.ToAuthorization }
        }
    }
}
