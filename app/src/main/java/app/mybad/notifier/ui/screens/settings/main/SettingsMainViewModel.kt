package app.mybad.notifier.ui.screens.settings.main

import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.user.LoadUserSettingsUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsMainViewModel @Inject constructor(
    private val userSettingsUseCase: LoadUserSettingsUseCase
) :
    BaseViewModel<SettingsMainScreenContract.Event, SettingsMainScreenContract.State, SettingsMainScreenContract.Effect>() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val userModel = userSettingsUseCase.getUserPersonal()
            launch(Dispatchers.Main) {
                setState { copy(userAvatar = userModel.avatar ?: "") }
            }
        }
    }

    override fun setInitialState(): SettingsMainScreenContract.State {
        return SettingsMainScreenContract.State(
            userAvatar = ""
        )
    }

    override fun handleEvents(event: SettingsMainScreenContract.Event) {
        when (event) {
            SettingsMainScreenContract.Event.EditAvatarClicked -> setEffect { SettingsMainScreenContract.Effect.Navigation.ToAvatarEdit }
            SettingsMainScreenContract.Event.ProfileClicked -> setEffect { SettingsMainScreenContract.Effect.Navigation.ToProfile }
            SettingsMainScreenContract.Event.NotificationsSettingsClicked -> setEffect { SettingsMainScreenContract.Effect.Navigation.ToNotificationsSettings }
            SettingsMainScreenContract.Event.LeaveWishesClicked -> setEffect { SettingsMainScreenContract.Effect.Navigation.ToLeaveWishes }
            SettingsMainScreenContract.Event.AboutClicked -> setEffect { SettingsMainScreenContract.Effect.Navigation.ToAbout }
        }
    }
}