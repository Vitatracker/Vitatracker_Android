package app.mybad.notifier.ui.screens.settings.main

import androidx.lifecycle.viewModelScope
import app.mybad.domain.scheduler.NotificationsScheduler
import app.mybad.domain.usecases.user.ClearDBUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsMainViewModel @Inject constructor(
    private val clearDBUseCase: ClearDBUseCase,
    private val notificationsScheduler: NotificationsScheduler, // TODO("только для теста")
) : BaseViewModel<
    SettingsMainScreenContract.Event,
    SettingsMainScreenContract.State,
    SettingsMainScreenContract.Effect>() {

    override fun setInitialState() = SettingsMainScreenContract.State

    override fun handleEvents(event: SettingsMainScreenContract.Event) {
        when (event) {
            SettingsMainScreenContract.Event.ProfileClicked -> setEffect {
                SettingsMainScreenContract.Effect.Navigation.ToProfile
            }

            SettingsMainScreenContract.Event.SystemNotificationsSettingsClicked -> setEffect {
                SettingsMainScreenContract.Effect.Navigation.ToSystemNotificationsSettings
            }

            SettingsMainScreenContract.Event.LeaveWishesClicked -> setEffect {
                SettingsMainScreenContract.Effect.Navigation.ToLeaveWishes
            }

            SettingsMainScreenContract.Event.AboutClicked -> setEffect {
                SettingsMainScreenContract.Effect.Navigation.ToAbout
            }

            SettingsMainScreenContract.Event.ClearDB -> {
                viewModelScope.launch {
                    clearDBUseCase()
                }
            }
            SettingsMainScreenContract.Event.SetAlarm -> {
                viewModelScope.launch {
                    notificationsScheduler.addAlarmTest()
                }
            }
        }
    }
}
