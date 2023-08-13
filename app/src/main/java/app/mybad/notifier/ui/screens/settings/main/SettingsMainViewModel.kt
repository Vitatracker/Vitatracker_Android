package app.mybad.notifier.ui.screens.settings.main

import androidx.lifecycle.viewModelScope
import app.mybad.domain.usecases.user.ClearDBUseCase
import app.mybad.domain.usecases.user.GetUserPersonalUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsMainViewModel @Inject constructor(
    private val getUserPersonalUseCase: GetUserPersonalUseCase,
    private val clearDBUseCase: ClearDBUseCase,
) : BaseViewModel<
        SettingsMainContract.Event,
        SettingsMainContract.State,
        SettingsMainContract.Effect>() {

    init {
        viewModelScope.launch {
            val userModel = getUserPersonalUseCase()
            setState { copy(userAvatar = userModel.avatar ?: "") }
        }
    }

    override fun setInitialState() = SettingsMainContract.State()

    override fun handleEvents(event: SettingsMainContract.Event) {
        when (event) {
            SettingsMainContract.Event.EditAvatarClicked -> setEffect {
                SettingsMainContract.Effect.Navigation.ToAvatarEdit
            }

            SettingsMainContract.Event.ProfileClicked -> setEffect {
                SettingsMainContract.Effect.Navigation.ToProfile
            }

            SettingsMainContract.Event.NotificationsSettingsClicked -> setEffect {
                SettingsMainContract.Effect.Navigation.ToNotificationsSettings
            }

            SettingsMainContract.Event.LeaveWishesClicked -> setEffect {
                SettingsMainContract.Effect.Navigation.ToLeaveWishes
            }

            SettingsMainContract.Event.AboutClicked -> setEffect {
                SettingsMainContract.Effect.Navigation.ToAbout
            }

            SettingsMainContract.Event.ClearDB -> clearDB()
        }
    }

    private fun clearDB() {
        viewModelScope.launch {
            clearDBUseCase()
        }
    }
}
