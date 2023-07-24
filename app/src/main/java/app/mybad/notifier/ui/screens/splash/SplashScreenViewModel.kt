package app.mybad.notifier.ui.screens.splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.DataStoreUseCase
import app.mybad.domain.usecases.user.GetUsersCountUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase,
    private val getUsersCountUseCase: GetUsersCountUseCase
) : BaseViewModel<SplashScreenContract.Event, SplashScreenContract.State, SplashScreenContract.Effect>() {

    init {
        Log.w("VTTAG", "SplashScreenViewModel init")
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreUseCase.token.collect { token ->
                Log.w("VTTAG", "SplashScreenViewModel::observeDataStore: token=$token")
                AuthToken.token = token
                if (token.isNotBlank()) {
                    // check token and try to get new
                    setEffect { SplashScreenContract.Effect.Navigation.ToMain }
                } else if (getUsersCountUseCase.execute() == 0) {
                    viewModelScope.launch(Dispatchers.Main) {
                        setState { copy(startButtonVisible = true) }
                    }
                } else {
                    setEffect { SplashScreenContract.Effect.Navigation.ToAuthorization }
                }
            }
        }
    }

    override fun setInitialState(): SplashScreenContract.State {
        return SplashScreenContract.State(startButtonVisible = false)
    }

    override fun handleEvents(event: SplashScreenContract.Event) {
        when (event) {
            SplashScreenContract.Event.OnStartClicked -> {
                setEffect { SplashScreenContract.Effect.Navigation.ToAuthorization }
            }
        }
    }
}