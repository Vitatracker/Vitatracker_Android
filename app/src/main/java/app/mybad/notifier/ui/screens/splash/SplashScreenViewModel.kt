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

    private val _screenState: MutableStateFlow<SplashScreenState> = MutableStateFlow(SplashScreenState.Initial)
    val screenState = _screenState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreUseCase.token.collect {
                Log.w("VTTAG", "MainActivityViewModel::observeDataStore: token=$it")
                AuthToken.token = it
                val userAuthorized = it.isNotBlank()
                if (userAuthorized) {
                    // check token and try to get new
                    _screenState.value = SplashScreenState.Authorized
                } else {
                    _screenState.value = SplashScreenState.NotAuthorized
                }
            }
        }
    }
}