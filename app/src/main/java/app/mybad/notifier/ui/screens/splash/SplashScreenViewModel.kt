package app.mybad.notifier.ui.screens.splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.user.GetUsersCountUseCase
import app.mybad.domain.usecases.user.TakeUserAuthTokenUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.utils.currentDateTimeInSeconds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val getUsersCountUseCase: GetUsersCountUseCase,
    private val takeUserAuthTokenUseCase: TakeUserAuthTokenUseCase,
) : BaseViewModel<SplashScreenContract.Event, SplashScreenContract.State, SplashScreenContract.Effect>() {

    init {
        viewModelScope.launch {
            // прочитать токен, если записан
            readToken()

            Log.w(
                "VTTAG",
                "SplashScreenViewModel:: AuthToken.isAuthorize=${AuthToken.isAuthorize.value}"
            )
            if (AuthToken.isAuthorize.value) {
                Log.w("VTTAG", "SplashScreenViewModel:: Navigation.ToMain")
                // перейти на главный экран
                setEffect { SplashScreenContract.Effect.Navigation.ToMain }
            } else {
                if (getUsersCountUseCase() == 0L) {
                    setState { copy(startButtonVisible = true) }
                } else {
                    Log.w("VTTAG", "SplashScreenViewModel:: Navigation.ToAuthorization")
                    // перейти на авторизацию
                    setEffect { SplashScreenContract.Effect.Navigation.ToAuthorization }
                }
            }
        }
    }

    override fun setInitialState() = SplashScreenContract.State()

    override fun handleEvents(event: SplashScreenContract.Event) {
        when (event) {
            SplashScreenContract.Event.OnAuthorization -> {
                setEffect { SplashScreenContract.Effect.Navigation.ToAuthorization }
            }
        }
    }

    private suspend fun readToken() {
        takeUserAuthTokenUseCase(currentDate = currentDateTimeInSeconds())
        Log.w("VTTAG", "SplashScreenViewModel:: readToken: userId=${AuthToken.userId}")
    }

}
