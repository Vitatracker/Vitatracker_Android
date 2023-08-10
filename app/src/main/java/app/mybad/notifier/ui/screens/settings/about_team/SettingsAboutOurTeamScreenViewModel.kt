package app.mybad.notifier.ui.screens.settings.about_team

import app.mybad.notifier.ui.base.BaseViewModel

class SettingsAboutOurTeamScreenViewModel :
    BaseViewModel<SettingsAboutOurTeamScreenContract.Event, SettingsAboutOurTeamScreenContract.State, SettingsAboutOurTeamScreenContract.Effect>() {
    override fun setInitialState(): SettingsAboutOurTeamScreenContract.State = SettingsAboutOurTeamScreenContract.State

    override fun handleEvents(event: SettingsAboutOurTeamScreenContract.Event) {
        when (event) {
            SettingsAboutOurTeamScreenContract.Event.ActionBack -> setEffect { SettingsAboutOurTeamScreenContract.Effect.Navigation.Back }
        }
    }
}