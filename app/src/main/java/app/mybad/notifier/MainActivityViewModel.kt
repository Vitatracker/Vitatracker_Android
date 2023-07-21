package app.mybad.notifier

import android.content.Context
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
import app.mybad.domain.usecases.user.TakeUserAuthTokenUseCase
import app.mybad.domain.usecases.user.UpdateUserNotificationUseCase
import app.mybad.domain.usecases.user.UpdateUserPersonalUseCase
import app.mybad.domain.usecases.user.UpdateUserRulesUseCase
import app.mybad.theme.utils.getCurrentDateTime
import app.mybad.theme.utils.toEpochSecond
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getCoursesUseCase: GetCoursesUseCase,
    private val takeUserAuthTokenUseCase: TakeUserAuthTokenUseCase,
    private val clearUserAuthTokenUseCase: ClearUserAuthTokenUseCase,
    private val userRulesUseCase: UpdateUserRulesUseCase,
    private val userPersonalUseCase: UpdateUserPersonalUseCase,
    private val userNotificationUseCase: UpdateUserNotificationUseCase,
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
) : ViewModel() {

    val isAuthorize = AuthToken.isAuthorize.also {
        if (it.value) readData()
    }

    // worker синхронизации данных
    private val workSync = WorkManager.getInstance(context)

    init {
        Log.w("VTTAG", "MainActivityViewModel::init: app start")
        readToken()
        workSync.start()
    }

    private fun readToken() {
        viewModelScope.launch {
            takeUserAuthTokenUseCase(currentDate = getCurrentDateTime().toEpochSecond())
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
