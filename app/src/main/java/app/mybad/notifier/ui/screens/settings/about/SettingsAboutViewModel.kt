package app.mybad.notifier.ui.screens.settings.about

import app.mybad.notifier.ui.base.BaseViewModel

class SettingsAboutViewModel : BaseViewModel<
        SettingsAboutContract.Event,
        SettingsAboutContract.State,
        SettingsAboutContract.Effect>() {
    override fun setInitialState() = SettingsAboutContract.State

    override fun handleEvents(event: SettingsAboutContract.Event) {
        when (event) {
            SettingsAboutContract.Event.ActionBack -> {
                setEffect { SettingsAboutContract.Effect.Navigation.Back }
            }
            SettingsAboutContract.Event.AboutOurTeam -> {
                setEffect { SettingsAboutContract.Effect.Navigation.AboutOurTeam }
            }
            SettingsAboutContract.Event.PrivacyPolicyClicked -> {}
            SettingsAboutContract.Event.UserAgreementClicked -> {}
        }
    }
}
