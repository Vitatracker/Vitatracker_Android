package app.mybad.notifier.ui.screens.settings.notifications

import app.mybad.notifier.ui.base.BaseViewModel

class SettingsNotificationsScreenViewModel :
    BaseViewModel<SettingsNotificationsScreenContract.Event, SettingsNotificationsScreenContract.State, SettingsNotificationsScreenContract.Effect>() {
    override fun setInitialState(): SettingsNotificationsScreenContract.State = SettingsNotificationsScreenContract.State

    override fun handleEvents(event: SettingsNotificationsScreenContract.Event) {
        when (event) {
            SettingsNotificationsScreenContract.Event.ActionBack -> setEffect { SettingsNotificationsScreenContract.Effect.Navigation.Back }
            SettingsNotificationsScreenContract.Event.CheckSettingsClicked -> setEffect { SettingsNotificationsScreenContract.Effect.CheckSettings }
            SettingsNotificationsScreenContract.Event.ContactUsClicked -> setEffect { SettingsNotificationsScreenContract.Effect.ContactUs }
            SettingsNotificationsScreenContract.Event.ReloadSettingsClicked -> setEffect { SettingsNotificationsScreenContract.Effect.ReloadSettings }
            SettingsNotificationsScreenContract.Event.SetupNotificationsClicked -> setEffect { SettingsNotificationsScreenContract.Effect.SetupNotifications }
            SettingsNotificationsScreenContract.Event.SetupSleepRegimeClicked -> setEffect { SettingsNotificationsScreenContract.Effect.SetupSleepRegime }
        }
    }
}