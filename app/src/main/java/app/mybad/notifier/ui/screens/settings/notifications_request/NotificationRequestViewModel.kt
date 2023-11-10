package app.mybad.notifier.ui.screens.settings.notifications_request

import app.mybad.notifier.ui.base.BaseViewModel

class NotificationRequestViewModel : BaseViewModel<
    NotificationRequestContract.Event,
    NotificationRequestContract.State,
    NotificationRequestContract.Effect>() {
    override fun setInitialState() = NotificationRequestContract.State

    override fun handleEvents(event: NotificationRequestContract.Event) {
        when (event) {
            NotificationRequestContract.Event.OnNext -> {
                setEffect { NotificationRequestContract.Effect.Navigation.ToNext }
            }

            NotificationRequestContract.Event.OnSettings -> {
                setEffect { NotificationRequestContract.Effect.Navigation.ToSettings }
            }
        }
    }
}
