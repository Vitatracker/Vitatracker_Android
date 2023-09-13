package app.mybad.notifier.ui.screens.settings.main

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SettingsMainScreenContract {
    sealed interface Event : ViewEvent {
        object ProfileClicked : Event
        object NotificationsSettingsClicked : Event
        object LeaveWishesClicked : Event
        object AboutClicked : Event
        object ClearDB : Event
    }

    object State: ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            object ToProfile : Navigation
            object ToNotificationsSettings : Navigation
            object ToLeaveWishes : Navigation
            object ToAbout : Navigation
        }
    }
}
