package app.mybad.notifier.ui.screens.settings.main

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SettingsMainScreenContract {
    sealed interface Event : ViewEvent {
        data object ProfileClicked : Event
        data object SystemNotificationsSettingsClicked : Event
        data object LeaveWishesClicked : Event
        data object AboutClicked : Event
    }

    object State : ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            data object ToProfile : Navigation
            data object ToSystemNotificationsSettings : Navigation
            data object ToLeaveWishes : Navigation
            data object ToAbout : Navigation
        }
    }
}
