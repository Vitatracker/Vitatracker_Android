package app.mybad.notifier.ui.screens.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.user.NotificationsUserDomainModel
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.models.user.UserSettingsDomainModel
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.usecases.DataStoreUseCase
import app.mybad.domain.usecases.user.DeleteUserModelUseCase
import app.mybad.domain.usecases.user.LoadUserSettingsUseCase
import app.mybad.domain.usecases.user.UpdateUserModelUseCase
import app.mybad.domain.usecases.user.UpdateUserNotificationDomainModelUseCase
import app.mybad.domain.usecases.user.UpdateUserPersonalDomainModelUseCase
import app.mybad.domain.usecases.user.UpdateUserRulesDomainModelUseCase
import app.mybad.domain.utils.ApiResult
import app.mybad.network.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userSettingsUseCase: LoadUserSettingsUseCase,
    private val userRulesDomainModelUseCase: UpdateUserRulesDomainModelUseCase,
    private val userPersonalDomainModelUseCase: UpdateUserPersonalDomainModelUseCase,
    private val userNotificationDomainModelUseCase: UpdateUserNotificationDomainModelUseCase,
    private val deleteUserModelUseCase: DeleteUserModelUseCase,
    private val updateUserModelUseCase: UpdateUserModelUseCase,
//    private val switchGlobalNotificationsUseCase: SwitchGlobalNotificationsUseCase,
    private val dataStore: DataStoreUseCase,

    private val coursesRepo: CoursesRepo,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getUserModel()
            delay(500)
            _state.emit(_state.value.copy(courses = coursesRepo.getAll(AuthToken.userId)))
            _state.emit(_state.value.copy(personalDomainModel = userSettingsUseCase.getUserPersonal()))
            _state.emit(_state.value.copy(notificationsUserDomainModel = userSettingsUseCase.getUserNotification()))
            _state.emit(_state.value.copy(rulesUserDomainModel = userSettingsUseCase.getUserRules()))
            _state.emit(_state.value.copy(userModel = setUserModelForRequest()))
        }
    }

    private suspend fun setUserModelForRequest(): UserDomainModel {
        return UserDomainModel(
            id = AuthToken.userId.toLong(),
            personal = _state.value.personalDomainModel,
            settings = UserSettingsDomainModel(
                notifications = _state.value.notificationsUserDomainModel,
                rules = _state.value.rulesUserDomainModel
            )
        )
    }

    private fun setUserModelFromBack(userModel: UserModel) {
        val model = UserDomainModel(
            id = userModel.id,
            personal = PersonalDomainModel(
                name = userModel.name,
                avatar = userModel.avatar,
                email = userModel.email
            ),
            settings = UserSettingsDomainModel(
                notifications = NotificationsUserDomainModel(
                    isEnabled = if (userModel.notificationSettings == null) false else userModel.notificationSettings!!.isEnabled,
                    isFloat = if (userModel.notificationSettings == null) false else userModel.notificationSettings!!.isFloat,
                    medicationControl = if (userModel.notificationSettings == null) false else userModel.notificationSettings!!.medicalControl,
                    nextCourseStart = if (userModel.notificationSettings == null) false else userModel.notificationSettings!!.nextCourseStart,
                    medsId = if (userModel.notificationSettings == null) emptyList() else userModel.notificationSettings!!.id
                )
            )
        )
        reduce(SettingsIntent.SetPersonal(personal = model.personal))
        reduce(SettingsIntent.SetNotifications(notifications = model.settings.notifications))
    }

    private suspend fun getUserModel() {
        val result = userSettingsUseCase.getUserModel()
        viewModelScope.launch {
            when (result) {
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
                    dataStore.clear()
                }

                is SettingsIntent.Exit -> {}
                is SettingsIntent.SetNotifications -> {
                    userNotificationDomainModelUseCase.execute(notificationsUserDomainModel = intent.notifications)
                    _state.emit(_state.value.copy(notificationsUserDomainModel = intent.notifications))
                    _state.emit(_state.value.copy(userModel = setUserModelForRequest()))
                    delay(100)
                    updateUserModelUseCase.execute(
                        userDomainModel =
                        state.value.userModel
                    )
                }

                is SettingsIntent.SetPersonal -> {
                    userPersonalDomainModelUseCase.execute(personalDomainModel = intent.personal)
                    _state.emit(_state.value.copy(personalDomainModel = intent.personal))
                    _state.emit(_state.value.copy(userModel = setUserModelForRequest()))
                    delay(100)
                    updateUserModelUseCase.execute(
                        userDomainModel =
                        state.value.userModel
                    )
                }

                is SettingsIntent.SetRules -> {
                    userRulesDomainModelUseCase.execute(rulesUserDomainModel = intent.rules)
                    _state.emit(_state.value.copy(rulesUserDomainModel = intent.rules))
                    _state.emit(_state.value.copy(userModel = setUserModelForRequest()))
                    delay(100)
                    updateUserModelUseCase.execute(
                        userDomainModel =
                        state.value.userModel
                    )
                }

                is SettingsIntent.ChangePassword -> {}
            }
        }
    }
}
