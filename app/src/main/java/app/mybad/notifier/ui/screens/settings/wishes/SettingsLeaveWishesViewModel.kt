package app.mybad.notifier.ui.screens.settings.wishes

import app.mybad.notifier.ui.base.BaseViewModel

class SettingsLeaveWishesViewModel : BaseViewModel<SettingsLeaveWishesContract.Event,
        SettingsLeaveWishesContract.State, SettingsLeaveWishesContract.Effect>() {
    override fun setInitialState() = SettingsLeaveWishesContract.State

    override fun handleEvents(event: SettingsLeaveWishesContract.Event) {
        when (event) {
            SettingsLeaveWishesContract.Event.ActionBack -> {
                setEffect { SettingsLeaveWishesContract.Effect.Navigation.Back }
            }

            SettingsLeaveWishesContract.Event.SendMail -> {
                setEffect { SettingsLeaveWishesContract.Effect.SendMail }
            }
        }
    }
}
