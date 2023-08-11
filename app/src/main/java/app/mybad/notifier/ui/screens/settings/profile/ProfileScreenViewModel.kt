package app.mybad.notifier.ui.screens.settings.profile

import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.user.LoadUserSettingsUseCase
import app.mybad.domain.usecases.user.UpdateUserPersonalUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val userSettingsUseCase: LoadUserSettingsUseCase,
    private val updateUserPersonalUseCase: UpdateUserPersonalUseCase
) : BaseViewModel<ProfileScreenContract.Event, ProfileScreenContract.State, ProfileScreenContract.Effect>() {

    init {
        setModelFromSaved()
    }

    private fun setModelFromSaved() {
        viewModelScope.launch(Dispatchers.IO) {
            val model = userSettingsUseCase.getUserPersonal()
            launch(Dispatchers.Main) {
                setState {
                    copy(
                        userAvatar = model.avatar ?: "",
                        name = model.name ?: "",
                        email = model.email ?: "",
                        isLoading = false
                    )
                }
            }
        }
    }

    override fun setInitialState(): ProfileScreenContract.State {
        return ProfileScreenContract.State(
            userAvatar = "",
            name = "",
            email = "",
            isLoading = true
        )
    }

    override fun handleEvents(event: ProfileScreenContract.Event) {
        when (event) {
            ProfileScreenContract.Event.ActionBack -> {
                setEffect { ProfileScreenContract.Effect.Navigation.Back }
            }

            ProfileScreenContract.Event.ChangePassword -> setEffect { ProfileScreenContract.Effect.Navigation.ToChangePassword }
            ProfileScreenContract.Event.EditAvatar -> setEffect { ProfileScreenContract.Effect.ShowDialog }
            is ProfileScreenContract.Event.OnUserNameChanged -> setState { copy(name = event.name) }
            ProfileScreenContract.Event.DeleteAccount -> deleteAccount()
            ProfileScreenContract.Event.SignOut -> signOut()
        }
    }

    private fun saveNewSettings() {

    }

    override fun onCleared() {
        viewModelScope.launch(Dispatchers.IO) {
            val model = userSettingsUseCase.getUserPersonal()
            updateUserPersonalUseCase.execute(model.copy(name = viewState.value.name))
            super.onCleared()
        }
    }

    private fun deleteAccount() {
//        TODO("Not yet implemented")
        setEffect { ProfileScreenContract.Effect.Navigation.ToAuthorization }
    }

    private fun signOut() {
        setEffect { ProfileScreenContract.Effect.Navigation.ToAuthorization }
//        TODO("Not yet implemented")
    }
}