package app.mybad.notifier.ui.screens.settings.main

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SettingsMainContract {
    sealed interface Event : ViewEvent {
        object EditAvatarClicked : Event
        object ProfileClicked : Event
        object NotificationsSettingsClicked : Event
        object LeaveWishesClicked : Event
        object AboutClicked : Event
        object ClearDB : Event
    }

    data class State(
        val userAvatar: String = "",
    ) : ViewState

    sealed interface Effect : ViewSideEffect {
        sealed interface Navigation : Effect {
            object ToAvatarEdit : Navigation
            object ToProfile : Navigation
            object ToNotificationsSettings : Navigation
            object ToLeaveWishes : Navigation
            object ToAbout : Navigation
        }
    }
}
