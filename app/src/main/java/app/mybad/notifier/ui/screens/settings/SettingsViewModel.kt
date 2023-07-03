package app.mybad.notifier.ui.screens.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.user.NotificationsUserDomainModel
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.models.user.UserSettingsDomainModel
import app.mybad.domain.usecases.DataStoreUseCase
import app.mybad.domain.usecases.courses.GetCoursesAllUseCase
import app.mybad.domain.usecases.user.DeleteUserModelUseCase
import app.mybad.domain.usecases.user.LoadUserSettingsUseCase
import app.mybad.domain.usecases.user.UpdateUserModelUseCase
import app.mybad.domain.usecases.user.UpdateUserNotificationUseCase
import app.mybad.domain.usecases.user.UpdateUserPersonalUseCase
import app.mybad.domain.usecases.user.UpdateUserRulesUseCase
import app.mybad.domain.utils.ApiResult
import app.mybad.network.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userSettingsUseCase: LoadUserSettingsUseCase,
    private val userRulesUseCase: UpdateUserRulesUseCase,
    private val userPersonalUseCase: UpdateUserPersonalUseCase,
    private val userNotificationUseCase: UpdateUserNotificationUseCase,
    private val deleteUserModelUseCase: DeleteUserModelUseCase,
    private val updateUserModelUseCase: UpdateUserModelUseCase,
//    private val switchGlobalNotificationsUseCase: SwitchGlobalNotificationsUseCase,

    private val dataStoreUseCase: DataStoreUseCase,
    private val getCoursesAllUseCase: GetCoursesAllUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val userPersonal = async { userSettingsUseCase.getUserPersonal() }
            val userNotification = async { userSettingsUseCase.getUserNotification() }
            val userRules = async { userSettingsUseCase.getUserRules() }
            val userModel = async { setUserModelForRequest() }
            val courseAll = getCoursesAllUseCase(AuthToken.userId).mapNotNull { result ->
                when (result) {
                    is ApiResult.ApiSuccess -> {
                        result.data as? List<CourseDomainModel>
                    }

                    is ApiResult.ApiError -> {
                        null
                    }

                    is ApiResult.ApiException -> {
                        null
                    }
                }

            }.last()
            _state.emit(
                _state.value.copy(
                    courses = courseAll,
                    personalDomainModel = userPersonal.await(),
                    notificationsUserDomainModel = userNotification.await(),
                    rulesUserDomainModel = userRules.await(),
                    userModel = userModel.await()
                )
            )
            getUserModel()
        }
    }

    private fun setUserModelForRequest() = UserDomainModel(
        id = AuthToken.userId,
        personal = _state.value.personalDomainModel,
        settings = UserSettingsDomainModel(
            notifications = _state.value.notificationsUserDomainModel,
            rules = _state.value.rulesUserDomainModel
        )
    )

    private fun setUserModelFromBack(userModel: UserModel) {
        val model = UserDomainModel(
            id = userModel.id,
            personal = PersonalDomainModel(
                name = userModel.name,
                avatar = userModel.avatar,
                email = userModel.email
            ),
            settings = userModel.notificationSettings?.run {
                UserSettingsDomainModel(
                    notifications = NotificationsUserDomainModel(
                        isEnabled = isEnabled,
                        isFloat = isFloat,
                        medicationControl = medicationControl,
                        nextCourseStart = nextCourseStart,
                        medsId = id
                    )
                )
            } ?: UserSettingsDomainModel()
        )
        reduce(SettingsIntent.SetPersonal(personal = model.personal))
        reduce(SettingsIntent.SetNotifications(notifications = model.settings.notifications))
    }

    private suspend fun getUserModel() {
        viewModelScope.launch {
            when (val result = userSettingsUseCase.getUserModel()) {
                is ApiResult.ApiSuccess -> setUserModelFromBack(result.data as UserModel)
                is ApiResult.ApiError -> Log.d("TAG", "error: ${result.code} ${result.message}")
                is ApiResult.ApiException -> Log.d("TAG", "exception: ${result.e}")
            }
        }
    }

    fun reduce(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is SettingsIntent.DeleteAccount -> {
                    deleteUserModelUseCase.execute(_state.value.userModel.id.toString())
                    dataStoreUseCase.clear()
                }

                is SettingsIntent.Exit -> {}
                is SettingsIntent.SetNotifications -> {
                    userNotificationUseCase.execute(notificationsUserDomainModel = intent.notifications)
                    _state.emit(_state.value.copy(notificationsUserDomainModel = intent.notifications))
                    _state.emit(_state.value.copy(userModel = setUserModelForRequest()))
                    delay(100)
                    updateUserModelUseCase.execute(
                        userDomainModel =
                        state.value.userModel
                    )
                }

                is SettingsIntent.SetPersonal -> {
                    userPersonalUseCase.execute(personalDomainModel = intent.personal)
                    _state.emit(_state.value.copy(personalDomainModel = intent.personal))
                    _state.emit(_state.value.copy(userModel = setUserModelForRequest()))
                    delay(100)
                    updateUserModelUseCase.execute(
                        userDomainModel =
                        state.value.userModel
                    )
                }

                is SettingsIntent.SetRules -> {
                    userRulesUseCase.execute(rulesUserDomainModel = intent.rules)
                    _state.emit(_state.value.copy(rulesUserDomainModel = intent.rules))
                    _state.emit(_state.value.copy(userModel = setUserModelForRequest()))
                    delay(100)
                    updateUserModelUseCase.execute(
                        userDomainModel = state.value.userModel
                    )
                }

                is SettingsIntent.ChangePassword -> {}
            }
        }
    }
}
