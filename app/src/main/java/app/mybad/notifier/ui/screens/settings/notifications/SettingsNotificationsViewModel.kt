package app.mybad.notifier.ui.screens.settings.notifications

import app.mybad.notifier.ui.base.BaseViewModel

class SettingsNotificationsViewModel : BaseViewModel<
        SettingsNotificationsContract.Event,
        SettingsNotificationsContract.State,
        SettingsNotificationsContract.Effect>() {
    override fun setInitialState() = SettingsNotificationsContract.State

    override fun handleEvents(event: SettingsNotificationsContract.Event) {
        when (event) {
            SettingsNotificationsContract.Event.ActionBack -> setEffect {
                SettingsNotificationsContract.Effect.Navigation.Back
            }

            SettingsNotificationsContract.Event.CheckSettingsClicked -> setEffect {
                SettingsNotificationsContract.Effect.CheckSettings
            }

            SettingsNotificationsContract.Event.ContactUsClicked -> setEffect {
                SettingsNotificationsContract.Effect.ContactUs
            }

            SettingsNotificationsContract.Event.ReloadSettingsClicked -> setEffect {
                SettingsNotificationsContract.Effect.ReloadSettings
            }

            SettingsNotificationsContract.Event.SetupNotificationsClicked -> setEffect {
                SettingsNotificationsContract.Effect.SetupNotifications
            }

            SettingsNotificationsContract.Event.SetupSleepRegimeClicked -> setEffect {
                SettingsNotificationsContract.Effect.SetupSleepRegime
            }
        }
    }
}
