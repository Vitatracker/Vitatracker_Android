package app.mybad.notifier

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.user.NotificationsUserDomainModel
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.models.user.UserSettingsDomainModel
import app.mybad.domain.usecases.DataStoreUseCase
import app.mybad.domain.usecases.courses.GetCoursesAllUseCase
import app.mybad.domain.usecases.settings.GetUserSettingsUseCase
import app.mybad.domain.usecases.user.UpdateUserNotificationUseCase
import app.mybad.domain.usecases.user.UpdateUserPersonalUseCase
import app.mybad.domain.usecases.user.UpdateUserRulesUseCase
import app.mybad.domain.utils.ApiResult
import app.mybad.network.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase,
    private val getCoursesAllUseCase: GetCoursesAllUseCase,
    private val userRulesUseCase: UpdateUserRulesUseCase,
    private val userPersonalUseCase: UpdateUserPersonalUseCase,
    private val userNotificationUseCase: UpdateUserNotificationUseCase,
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
) : ViewModel() {

    private val _isAuthorize = MutableStateFlow(false)
    val isAuthorize = _isAuthorize.asStateFlow()

    init {
        Log.w("VTTAG", "MainActivityViewModel::init: app start")
        readDataStore()
        observeDataStore()
    }

    private fun readDataStore() {
        viewModelScope.launch {
            AuthToken.token = dataStoreUseCase.token.first()
            AuthToken.refreshToken = dataStoreUseCase.refreshToken.first()
            AuthToken.userId = dataStoreUseCase.userId.first()
            AuthToken.email = dataStoreUseCase.email.first()
            Log.w("VTTAG", "MainActivityViewModel::readDataStore: userId=${AuthToken.userId}")
        }
    }

    private fun observeDataStore() {
        viewModelScope.launch {
            launch {
                dataStoreUseCase.token.collect {
                    Log.w("VTTAG", "MainActivityViewModel::observeDataStore: token=$it")
                    AuthToken.token = it
                    _isAuthorize.value = it.isNotBlank()
                    if (_isAuthorize.value) readData()
                }
            }
            launch {
                dataStoreUseCase.refreshToken.collect {
                    Log.w("VTTAG", "MainActivityViewModel::observeDataStore: refreshToken=$it")
                    AuthToken.refreshToken = it
                }
            }
            launch {
                dataStoreUseCase.userId.collect {
                    Log.w("VTTAG", "MainActivityViewModel::observeDataStore: userId=$it")
                    AuthToken.userId = it
                }
            }
            launch {
                dataStoreUseCase.email.collect {
                    Log.w("VTTAG", "MainActivityViewModel::observeDataStore: email=$it")
                    AuthToken.email = it
                }
            }
        }
    }

    fun clearDataStore() {
        viewModelScope.launch {
            dataStoreUseCase.clear()
        }
    }

    private suspend fun readData() {
        getCoursesAllUseCase()
        getAll()
    }

    private suspend fun getAll() {
        when (val settingsResult = getUserSettingsUseCase()) {
            is ApiResult.ApiSuccess -> setUserModelFromBack(settingsResult.data as UserModel)
            is ApiResult.ApiError -> Log.d(
                "VTTAG",
                "MainActivityViewModel::getAll: error=${settingsResult.code} ${settingsResult.message}"
            )

            is ApiResult.ApiException -> Log.d(
                "VTTAG",
                "MainActivityViewModel::getAll: exception=${settingsResult.e}"
            )
        }
    }

    private suspend fun setUserModelFromBack(userModel: UserModel) {
        val model = UserDomainModel(
            id = userModel.id,
            personal = PersonalDomainModel(
                name = userModel.name,
                avatar = userModel.avatar,
                email = userModel.email
            ),
            settings = UserSettingsDomainModel(
                notifications = NotificationsUserDomainModel(
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

    }

}
