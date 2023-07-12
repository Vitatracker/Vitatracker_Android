package app.mybad.notifier.ui.screens.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.DataStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase
) : ViewModel() {

    private val _effect: Channel<SplashScreenEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    private val _screenState: MutableStateFlow<SplashScreenState> = MutableStateFlow(SplashScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    init {
        Log.w("VTTAG", "SplashScreenViewModel init")
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreUseCase.token.collect {
                Log.w("VTTAG", "SplashScreenViewModel::observeDataStore: token=$it")
                AuthToken.token = it
                val userAuthorized = it.isNotBlank()
                if (userAuthorized) {
                    // check token and try to get new
                    _effect.send(SplashScreenEffect.NavigateToMain)
                } else {
                    _screenState.value = SplashScreenState.NotAuthorized
                }
            }
        }
    }

    fun onBeginClicked() {
        viewModelScope.launch {
            _effect.send(SplashScreenEffect.NavigateNext)
        }
    }

}