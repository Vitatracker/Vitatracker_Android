package app.mybad.notifier.ui.screens.settings.profile

import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.user.ClearUserAuthTokenUseCase
import app.mybad.domain.usecases.user.DeleteUserAccountUseCase
import app.mybad.domain.usecases.user.GetUserPersonalDbUseCase
import app.mybad.domain.usecases.user.UpdateUserNameDbUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsProfileViewModel @Inject constructor(
    private val getUserPersonalDbUseCase: GetUserPersonalDbUseCase,
    private val updateUserNameDbUseCase: UpdateUserNameDbUseCase,
    private val clearUserAuthTokenUseCase: ClearUserAuthTokenUseCase,
    private val deleteUserAccountUseCase: DeleteUserAccountUseCase,
) : BaseViewModel<SettingsProfileContract.Event, SettingsProfileContract.State, SettingsProfileContract.Effect>() {

    init {
        setModelFromSaved()
    }

    private fun setModelFromSaved() {
        viewModelScope.launch {
            val model = getUserPersonalDbUseCase()
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
                viewModelScope.launch {
                    updateUserNameDbUseCase(userName = viewState.value.name)
                    setEffect { SettingsProfileContract.Effect.Navigation.Back }
                }
            }

            SettingsProfileContract.Event.ChangePassword -> {
                setEffect { SettingsProfileContract.Effect.Navigation.ToChangePassword }
            }

            is SettingsProfileContract.Event.OnUserNameChanged -> {
                setState { copy(name = event.name) }
            }

            SettingsProfileContract.Event.DeleteAccount -> deleteAccount()

            SettingsProfileContract.Event.DeleteConfirmation -> {
                setState { copy(confirmDelete = true) }
            }

            SettingsProfileContract.Event.Cancel -> {
                setState { copy(confirmDelete = false) }
            }

            SettingsProfileContract.Event.SignOut -> signOut()
        }
    }

    private fun deleteAccount() {
//        TODO("Not yet implemented")
        viewModelScope.launch {
            if (deleteUserAccountUseCase()) {
                setEffect { SettingsProfileContract.Effect.Navigation.ToAuthorization }
            } else {
                setState { copy(confirmDelete = false) }
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
