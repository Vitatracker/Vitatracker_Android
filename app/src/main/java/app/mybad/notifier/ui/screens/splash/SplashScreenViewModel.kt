package app.mybad.notifier.ui.screens.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.DataStoreUseCase
import app.mybad.domain.usecases.user.GetUsersCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase,
    private val getUsersCountUseCase: GetUsersCountUseCase
) : ViewModel() {

    private val _effect: Channel<SplashScreenEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        Log.w("VTTAG", "SplashScreenViewModel init")
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreUseCase.token.collect {
                Log.w("VTTAG", "SplashScreenViewModel::observeDataStore: token=$it")
                AuthToken.token = it
                val usersCount = getUsersCountUseCase.execute()
                val userAuthorized = it.isNotBlank()
                if (userAuthorized) {
                    // check token and try to get new
                    _effect.send(SplashScreenEffect.NavigateToMain)
                } else if (usersCount == 0) {
                    _effect.send(SplashScreenEffect.ShowButton)
                } else {
                    _effect.send(SplashScreenEffect.NavigateToAuthorization)
                }
            }
        }
    }

    fun onBeginClicked() {
        viewModelScope.launch {
            _effect.send(SplashScreenEffect.NavigateToAuthorization)
        }
    }

}