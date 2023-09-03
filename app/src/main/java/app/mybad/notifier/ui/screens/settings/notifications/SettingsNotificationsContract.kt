package app.mybad.notifier.ui.screens.settings.notifications

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SettingsNotificationsContract {
    sealed class Event : ViewEvent {
        object ActionBack : Event()
        object SetupNotificationsClicked : Event()
        object SetupSleepRegimeClicked : Event()
        object ContactUsClicked : Event()
    }

    object State : ViewState

    sealed class Effect : ViewSideEffect {
        object SetupNotifications : Effect()
        object SetupSleepRegime : Effect()
        object ContactUs : Effect()

        sealed class Navigation : Effect() {
            object Back : Navigation()
        }
    }
}
