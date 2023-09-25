package app.mybad.notifier

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import app.mybad.data.repos.SynchronizationCourseWorker.Companion.cancel
import app.mybad.data.repos.SynchronizationCourseWorker.Companion.start
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.courses.SynchronizationCourseUseCase
import app.mybad.domain.usecases.usages.SendUsageToNetworkUseCase
import app.mybad.domain.usecases.user.CheckDarkThemeUseCase
import app.mybad.utils.currentDateTimeUTCInSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val checkDarkThemeUseCase: CheckDarkThemeUseCase,
    private val sendUsageToNetworkUseCase: SendUsageToNetworkUseCase,
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

    @OptIn(FlowPreview::class)
    private fun observeAuthorizationAndSynchronization() {
        viewModelScope.launch {
            log("observeAuthorize: start")
            launch {
                AuthToken.isAuthorize.collectLatest { isAuthorize ->
                    log("observeAuthorize: isAuthorize=$isAuthorize")
                    if (isAuthorize) {
                        startSynchronizationWithServer()
                    } else {
                        cancelSynchronizationWithServer()
                    }
                }
            }
            launch {
                AuthToken.synchronization
                    .debounce(20000)
                    .collectLatest { time ->
                        log("synchronization: date=${time}")
                        synchronizationCourseUseCase(time).onFailure {
                            // TODO("отобразить ошибку синхронизации")
                        }
                    }
            }
            launch {
                AuthToken.updateUsage
                    .debounce(20000)
                    .collectLatest { (userId, usageId) ->
                        log("updateUsage: userId=$userId usageId=$usageId")
                        sendUsageToNetworkUseCase(userId, usageId).onFailure {
                            // TODO("отобразить ошибку синхронизации")
                        }
                    }
            }
            log("observeAuthorize: end")
        }
    }

    private suspend fun startSynchronizationWithServer() {
        log("startSynchronizationWithServer: start tokenDate=${AuthToken.tokenDate} tokenRefreshDate=${AuthToken.tokenRefreshDate} token=${AuthToken.token}")
        // первоначальная синхронизация
        AuthToken.requiredSynchronize(currentDateTimeUTCInSecond())
        worker.start()
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
