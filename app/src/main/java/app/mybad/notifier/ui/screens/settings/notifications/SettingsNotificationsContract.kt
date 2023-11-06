package app.mybad.notifier.ui.screens.settings.notifications

import app.mybad.notifier.ui.base.ViewEvent
import app.mybad.notifier.ui.base.ViewSideEffect
import app.mybad.notifier.ui.base.ViewState

class SettingsNotificationsContract {
    sealed interface Event : ViewEvent {
        object ActionBack : Event
        object SetupNotificationsClicked : Event
        object SetupNotificationsTrackerClicked : Event
        object SetupNotificationsInfoClicked : Event
        object SetupAlarmsClicked : Event
        object SetupSleepRegimeClicked : Event
        object ContactUsClicked : Event
    }

    object State : ViewState

    sealed interface Effect : ViewSideEffect {
        object SetupNotifications : Effect
        object SetupNotificationsTracker : Effect
        object SetupNotificationsInfo : Effect
        object SetupAlarms : Effect
        object SetupSleepRegime : Effect
        object ContactUs : Effect

        sealed interface Navigation : Effect {
            object Back : Navigation
        }
    }
}
