package app.mybad.notifier.ui.screens.splash

sealed class SplashScreenState {
    object Initial : SplashScreenState()
    object Authorized : SplashScreenState()
    object NotAuthorized : SplashScreenState()
}