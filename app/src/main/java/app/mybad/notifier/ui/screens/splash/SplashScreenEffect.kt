package app.mybad.notifier.ui.screens.splash

sealed class SplashScreenEffect {
    object NavigateToAuthorization : SplashScreenEffect()
    object NavigateToMain : SplashScreenEffect()
    object ShowButton : SplashScreenEffect()
}