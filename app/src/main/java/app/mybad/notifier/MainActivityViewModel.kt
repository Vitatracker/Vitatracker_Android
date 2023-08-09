package app.mybad.notifier

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import app.mybad.data.repos.SynchronizationCourseWorker.Companion.start
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.usecases.courses.GetCoursesUseCase
import app.mybad.domain.usecases.settings.GetUserSettingsUseCase
import app.mybad.domain.usecases.user.ClearUserAuthTokenUseCase
import app.mybad.domain.usecases.user.RefreshAuthTokenUseCase
import app.mybad.domain.usecases.user.TakeUserAuthTokenUseCase
import app.mybad.domain.usecases.user.UpdateUserNotificationUseCase
import app.mybad.domain.usecases.user.UpdateUserPersonalUseCase
import app.mybad.domain.usecases.user.UpdateUserRulesUseCase
import app.mybad.utils.currentDateTimeInSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val takeUserAuthTokenUseCase: TakeUserAuthTokenUseCase,
    private val clearUserAuthTokenUseCase: ClearUserAuthTokenUseCase,
    private val refreshAuthTokenUseCase: RefreshAuthTokenUseCase,
    private val userRulesUseCase: UpdateUserRulesUseCase,
    private val userPersonalUseCase: UpdateUserPersonalUseCase,
    private val userNotificationUseCase: UpdateUserNotificationUseCase,
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val workSync: WorkManager,
) : ViewModel() {

    val isAuthorize = AuthToken.isAuthorize.onEach {
        Log.w("VTTAG", "MainActivityViewModel::init: isAuthorize=${it}")
        if (it) {
            readData()
            // worker синхронизации данных
            workSync.start()
        }
    }

    init {
        Log.w("VTTAG", "MainActivityViewModel::init: app start")
        readToken()
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

    private fun readData() {
        viewModelScope.launch {
            Log.w("VTTAG", "MainActivityViewModel::readData: userId=${AuthToken.userId}")
            getCoursesUseCase()
            getAll()
        }
    }

    private suspend fun getAll() {
        getUserSettingsUseCase().onSuccess { result ->
            setUserModelFromBack(result)
        }.onFailure {

        }
    }

    //TODO("что это и для чего")
    private suspend fun setUserModelFromBack(userModel: UserDomainModel) {
        /*
                val model = UserSettingsDomainModel(
                    id = userModel.id,
                    personal = PersonalDomainModel(
                        name = userModel.name,
                        avatar = userModel.avatar,
                        email = userModel.email
                    ),
                    settings = UserNotificationDomainModel(
                        notifications = NotificationSettingDomainModel(
                            isEnabled = if (userModel.notificationSettings == null) false
                            else userModel.notificationSettings!!.isEnabled,
                            isFloat = if (userModel.notificationSettings == null) false
                            else userModel.notificationSettings!!.isFloat,
                            medicationControl = if (userModel.notificationSettings == null) false
                            else userModel.notificationSettings!!.medicationControl,
                            nextCourseStart = if (userModel.notificationSettings == null) false
                            else userModel.notificationSettings!!.nextCourseStart,
                            medsId = if (userModel.notificationSettings == null) emptyList()
                            else userModel.notificationSettings!!.id
                        )
                    )
                )

                userNotificationUseCase.execute(model.settings.notifications)
                userPersonalUseCase.execute(model.personal)
                userRulesUseCase.execute(model.settings.rules)

        */
    }

}
