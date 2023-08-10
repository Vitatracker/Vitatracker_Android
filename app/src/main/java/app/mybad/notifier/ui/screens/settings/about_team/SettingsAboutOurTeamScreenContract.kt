package app.mybad.notifier.ui.screens.settings.about_team

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SettingsAboutOurTeamScreenContract {
    sealed class Event : ViewEvent {
        object ActionBack : Event()
    }

    object State : ViewState

    sealed class Effect : ViewSideEffect {

        sealed class Navigation : Effect() {
            object Back : Navigation()
        }
    }
}