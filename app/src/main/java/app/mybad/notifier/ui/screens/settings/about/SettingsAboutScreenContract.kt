package app.mybad.notifier.ui.screens.settings.about

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SettingsAboutScreenContract {
    sealed class Event : ViewEvent {
        object ActionBack : Event()
        object AboutOurTeam : Event()
        object UserAgreementClicked : Event()
        object PrivacyPolicyClicked : Event()
    }

    object State : ViewState

    sealed class Effect : ViewSideEffect {

        sealed class Navigation : Effect() {
            object Back : Navigation()
            object AboutOurTeam : Navigation()
        }
    }
}