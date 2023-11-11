package app.mybad.notifier.ui.screens.settings.notifications_request

import app.mybad.notifier.ui.base.BaseViewModel

class NotificationRequestViewModel : BaseViewModel<
    NotificationRequestContract.Event,
    NotificationRequestContract.State,
    NotificationRequestContract.Effect>() {
    override fun setInitialState() = NotificationRequestContract.State()

    override fun handleEvents(event: NotificationRequestContract.Event) {
        when (event) {
            NotificationRequestContract.Event.OnNext -> {
                showConfirmation(false)
                setEffect { NotificationRequestContract.Effect.Navigation.ToNext }
            }

            NotificationRequestContract.Event.OnSettings -> {
                showConfirmation(false)
                setEffect { NotificationRequestContract.Effect.Navigation.ToSettings }
            }

            NotificationRequestContract.Event.OnReject -> showConfirmation(true)
        }
    }

    private fun showConfirmation(show: Boolean) {
        setState { copy(isConfirmation = show) }
    }
}
