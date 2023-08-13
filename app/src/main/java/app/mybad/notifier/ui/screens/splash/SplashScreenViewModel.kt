package app.mybad.notifier.ui.screens.splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import app.mybad.data.repos.SynchronizationCourseWorker.Companion.start
import app.mybad.domain.models.AuthToken
import app.mybad.domain.usecases.courses.GetCoursesUseCase
import app.mybad.domain.usecases.courses.SynchronizationCourseUseCase
import app.mybad.domain.usecases.settings.GetUserSettingsUseCase
import app.mybad.domain.usecases.user.ClearUserAuthTokenUseCase
import app.mybad.domain.usecases.user.GetUsersCountUseCase
import app.mybad.domain.usecases.user.TakeUserAuthTokenUseCase
import app.mybad.notifier.ui.base.BaseViewModel
import app.mybad.utils.currentDateTimeInSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val getUsersCountUseCase: GetUsersCountUseCase,

    private val getCoursesUseCase: GetCoursesUseCase,
    private val takeUserAuthTokenUseCase: TakeUserAuthTokenUseCase,
    private val clearUserAuthTokenUseCase: ClearUserAuthTokenUseCase,
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val synchronizationCourseUseCase: SynchronizationCourseUseCase,
    private val workSync: WorkManager,
) : BaseViewModel<SplashScreenContract.Event, SplashScreenContract.State, SplashScreenContract.Effect>() {

    init {
        Log.w("VTTAG", "SplashScreenViewModel::init: app start")
        readToken()
        observeAuthorize()
    }

    override fun setInitialState() = SplashScreenContract.State()

    override fun handleEvents(event: SplashScreenContract.Event) {
        when (event) {
            SplashScreenContract.Event.OnStartClicked -> {
                Log.w("VTTAG", "SplashScreenViewModel::Event: Event.OnStartClicked")
                setEffect { SplashScreenContract.Effect.Navigation.ToAuthorization }
            }

            SplashScreenContract.Event.OnAuthorization -> {
                Log.w("VTTAG", "SplashScreenViewModel::Event: Event.OnAuthorization")
                setEffect { SplashScreenContract.Effect.Navigation.ToAuthorization }
            }
        }
    }

    private fun observeAuthorize() {
        viewModelScope.launch {
            AuthToken.isAuthorize.collectLatest { isAuthorize ->
                if (isAuthorize) {
                    // TODO("check token and try to get new")
                    // прочитать данные из локальной базы
//                    readData()
                    // worker синхронизации данных
                    launch {
                        delay(1000)
                        syncCourseToServer()
                    }
                    // перейти на главный экран
                    setEffect { SplashScreenContract.Effect.Navigation.ToMain }
                } else {
                    if (getUsersCountUseCase() == 0L) {
                        setState { copy(startButtonVisible = true) }
                    } else {
                        setEffect { SplashScreenContract.Effect.Navigation.ToAuthorization }
                    }
                }
            }
        }
    }

    private fun readToken() {
        viewModelScope.launch {
            takeUserAuthTokenUseCase(currentDate = currentDateTimeInSecond())
            Log.w("VTTAG", "MainActivityViewModel::readToken: userId=${AuthToken.userId}")
        }
    }

    fun clearToken() {
        viewModelScope.launch {
            clearUserAuthTokenUseCase()
        }
    }

    private suspend fun syncCourseToServer() {
        synchronizationCourseUseCase(currentDateTimeInSecond()).onFailure {
            // если ошибка то запустим воркер
            workSync.start()
        }
    }

    //TODO("не понятно для чего тут читать данные, скорей всего не нужно")
//    private fun readData() {
//        viewModelScope.launch {
//            Log.w("VTTAG", "MainActivityViewModel::readData: userId=${AuthToken.userId}")
//            getCoursesUseCase()
//        }
//    }


}
