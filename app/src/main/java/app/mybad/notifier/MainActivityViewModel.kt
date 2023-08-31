package app.mybad.notifier

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import app.mybad.data.repos.SynchronizationCourseWorker.Companion.cancel
import app.mybad.data.repos.SynchronizationCourseWorker.Companion.start
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.courses.SynchronizationCourseUseCase
import app.mybad.domain.usecases.user.CheckDarkThemeUseCase
import app.mybad.utils.currentDateTimeInSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val checkDarkThemeUseCase: CheckDarkThemeUseCase,
    private val synchronizationCourseUseCase: SynchronizationCourseUseCase,
    private val worker: WorkManager,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val isDarkTheme = AuthToken.isAuthorize.flatMapLatest {
        checkDarkThemeUseCase(AuthToken.userId)
    }

    init {
        log("init: app start")
        // следить за изменением авторизации
        observeAuthorizationAndSynchronization()
    }

    private fun observeAuthorizationAndSynchronization() {
        viewModelScope.launch {
            log("observeAuthorize: start")
            AuthToken.isAuthorize.collectLatest { isAuthorize ->
                log("observeAuthorize: isAuthorize=$isAuthorize")
                if (isAuthorize) {
                    startSynchronizationWithServer()
                } else {
                    cancelSynchronizationWithServer()
                }
            }
            log("observeAuthorize: end")
        }
    }

    private suspend fun startSynchronizationWithServer() {
        log("startSynchronizationWithServer: start tokenDate=${AuthToken.tokenDate} tokenRefreshDate=${AuthToken.tokenRefreshDate} token=${AuthToken.token}")
        worker.start()
        synchronizationCourseUseCase(currentDateTimeInSecond())
    }

    private fun cancelSynchronizationWithServer() {
        log("cancelSynchronizationWithServer: end")
        worker.cancel()
    }

    override fun onCleared() {
        log("onCleared: --------------------------------------")
        cancelSynchronizationWithServer()
        super.onCleared()
    }

    private fun log(text: String) {
        Log.w("VTTAG", "MainActivityViewModel::$text")
    }

}
