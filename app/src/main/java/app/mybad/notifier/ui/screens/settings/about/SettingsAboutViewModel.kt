package app.mybad.notifier.ui.screens.settings.about

import app.mybad.notifier.ui.base.BaseViewModel

class SettingsAboutViewModel :
    BaseViewModel<SettingsAboutScreenContract.Event, SettingsAboutScreenContract.State, SettingsAboutScreenContract.Effect>() {
    override fun setInitialState(): SettingsAboutScreenContract.State = SettingsAboutScreenContract.State
    override fun handleEvents(event: SettingsAboutScreenContract.Event) {
        when (event) {
            SettingsAboutScreenContract.Event.ActionBack -> setEffect { SettingsAboutScreenContract.Effect.Navigation.Back }
            SettingsAboutScreenContract.Event.AboutOurTeam -> setEffect { SettingsAboutScreenContract.Effect.Navigation.AboutOurTeam }
            SettingsAboutScreenContract.Event.PrivacyPolicyClicked -> {}
            SettingsAboutScreenContract.Event.UserAgreementClicked -> {}
        }
    }
}