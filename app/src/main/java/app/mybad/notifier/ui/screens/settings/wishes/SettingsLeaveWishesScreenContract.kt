package app.mybad.notifier.ui.screens.settings.wishes

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SettingsLeaveWishesScreenContract {
    sealed class Event : ViewEvent {
        object ActionBack : Event()
        object SendMail : Event()
    }

    object State : ViewState

    sealed class Effect : ViewSideEffect {
        object SendMail : Effect()
        sealed class Navigation : Effect() {
            object Back : Navigation()
        }
    }
}