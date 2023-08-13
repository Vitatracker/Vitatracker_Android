package app.mybad.notifier.ui.screens.settings.wishes

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SettingsLeaveWishesContract {
    sealed interface Event : ViewEvent {
        object ActionBack : Event
        object SendMail : Event
    }

    object State : ViewState

    sealed interface Effect : ViewSideEffect {
        object SendMail : Effect
        sealed interface Navigation : Effect {
            object Back : Navigation
        }
    }
}
