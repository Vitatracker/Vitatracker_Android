package app.mybad.notifier.ui.screens.settings.wishes

import app.mybad.notifier.ui.base.BaseViewModel

class SettingsLeaveWishesScreenViewModel :
    BaseViewModel<SettingsLeaveWishesScreenContract.Event, SettingsLeaveWishesScreenContract.State, SettingsLeaveWishesScreenContract.Effect>() {
    override fun setInitialState(): SettingsLeaveWishesScreenContract.State = SettingsLeaveWishesScreenContract.State

    override fun handleEvents(event: SettingsLeaveWishesScreenContract.Event) {
        when (event) {
            SettingsLeaveWishesScreenContract.Event.ActionBack -> setEffect { SettingsLeaveWishesScreenContract.Effect.Navigation.Back }
            SettingsLeaveWishesScreenContract.Event.SendMail -> setEffect { SettingsLeaveWishesScreenContract.Effect.SendMail }
        }
    }
}