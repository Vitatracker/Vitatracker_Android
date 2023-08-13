package app.mybad.notifier.ui.screens.settings.about_team

import app.mybad.notifier.ui.base.BaseViewModel

class SettingsAboutTeamViewModel : BaseViewModel<
    SettingsAboutTeamContract.Event,
    SettingsAboutTeamContract.State,
    SettingsAboutTeamContract.Effect>() {
    override fun setInitialState() = SettingsAboutTeamContract.State

    override fun handleEvents(event: SettingsAboutTeamContract.Event) {
        when (event) {
            SettingsAboutTeamContract.Event.ActionBack -> {
                setEffect { SettingsAboutTeamContract.Effect.Navigation.Back }
            }
        }
    }
}
