package app.mybad.notifier.ui.screens.settings.about_team

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SettingsAboutTeamContract {
    sealed interface Event : ViewEvent {
        object ActionBack : Event
    }

    object State : ViewState

    sealed interface Effect : ViewSideEffect {

        sealed interface Navigation : Effect {
            object Back : Navigation
        }
    }
}
