package app.mybad.notifier.ui.screens.settings.main

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SettingsMainScreenContract {
    sealed class Event : ViewEvent {
        object EditAvatarClicked : Event()
        object ProfileClicked : Event()
        object NotificationsSettingsClicked : Event()
        object LeaveWishesClicked : Event()
        object AboutClicked : Event()
    }

    data class State(val userAvatar: String) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation : Effect() {
            object ToAvatarEdit : Navigation()
            object ToProfile : Navigation()
            object ToNotificationsSettings : Navigation()
            object ToLeaveWishes : Navigation()
            object ToAbout : Navigation()
        }
    }
}