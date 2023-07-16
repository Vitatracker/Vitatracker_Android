package app.mybad.notifier.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.models.user.UserNotificationDomainModel
import app.mybad.domain.models.user.UserSettingsDomainModel
import app.mybad.domain.usecases.courses.GetCoursesUseCase
import app.mybad.domain.usecases.user.DeleteUserModelUseCase
import app.mybad.domain.usecases.user.GetUserSettingsUseCase
import app.mybad.domain.usecases.user.UpdateUserModelUseCase
import app.mybad.domain.usecases.user.UpdateUserNotificationUseCase
import app.mybad.domain.usecases.user.UpdateUserPersonalUseCase
import app.mybad.domain.usecases.user.UpdateUserRulesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val updateUserModelUseCase: UpdateUserModelUseCase,
    private val deleteUserModelUseCase: DeleteUserModelUseCase,

    private val updateUserRulesUseCase: UpdateUserRulesUseCase,
    private val updateUserPersonalUseCase: UpdateUserPersonalUseCase,
    private val updateUserNotificationUseCase: UpdateUserNotificationUseCase,
//    private val switchGlobalNotificationsUseCase: SwitchGlobalNotificationsUseCase,

    private val getCoursesUseCase: GetCoursesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val userPersonal = async { getUserSettingsUseCase.getUserPersonal() }
            val userNotification = async { getUserSettingsUseCase.getUserNotification() }
            val userRules = async { getUserSettingsUseCase.getUserRules() }
            val userModel = async { setUserModelForRequest() }
            val courses = getCoursesUseCase(AuthToken.userId).getOrElse {
                TODO("Error: ${it.localizedMessage}")
            }
            _state.emit(
                _state.value.copy(
                    courses = courses,
                    personalDomainModel = userPersonal.await(),
                    notificationsUserDomainModel = userNotification.await(),
                    rulesUserDomainModel = userRules.await(),
                    userModel = userModel.await()
                )
            )
            getUserModel()
        }
    }

    private fun setUserModelForRequest() = UserSettingsDomainModel(
        id = AuthToken.userId,
        personal = _state.value.personalDomainModel,
        settings = UserNotificationDomainModel(
            notifications = _state.value.notificationsUserDomainModel,
            rules = _state.value.rulesUserDomainModel
        )
    )

    private fun setUserModelFromBack(user: UserDomainModel) {
        //TODO("setUserModelFromBack")
        /*
                val model = UserSettingsDomainModel(
                    id = user.id,
                    personal = UserPersonalDomainModel(
                        name = user.name,
                        avatar = user.avatar,
                        email = user.email
                    ),
                    settings = user.notificationSettings?.run {
                        UserNotificationDomainModel(
                            notifications = NotificationSettingDomainModel(
                                isEnabled = isEnabled,
                                isFloat = isFloat,
                                medicationControl = medicationControl,
                                nextCourseStart = nextCourseStart,
                                medsId = id
                            )
                        )
                    } ?: UserNotificationDomainModel()
                )
                reduce(SettingsIntent.SetPersonal(personal = model.personal))
                reduce(SettingsIntent.SetNotifications(notifications = model.settings.notifications))
        */
    }

    private suspend fun getUserModel() {
        viewModelScope.launch {
            getUserSettingsUseCase.getUserModel().onSuccess { user ->
                setUserModelFromBack(user)
            }
                .onFailure { error ->
                    //TODO("Error: getUserModel ${error.localizedMessage}")
                }
        }
    }

    fun reduce(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                is SettingsIntent.DeleteAccount -> {
                    deleteUserModelUseCase.execute(_state.value.userModel.id.toString())
                    AuthToken.clear()
                }

                is SettingsIntent.Exit -> {}
                is SettingsIntent.SetNotifications -> {
                    updateUserNotificationUseCase.execute(notificationsUserDomainModel = intent.notifications)
                    _state.emit(_state.value.copy(notificationsUserDomainModel = intent.notifications))
                    _state.emit(_state.value.copy(userModel = setUserModelForRequest()))
                    delay(100)
                    updateUserModelUseCase.execute(
                        userDomainModel =
                        state.value.userModel
                    )
                }

                is SettingsIntent.SetPersonal -> {
                    updateUserPersonalUseCase.execute(personalDomainModel = intent.personal)
                    _state.emit(_state.value.copy(personalDomainModel = intent.personal))
                    _state.emit(_state.value.copy(userModel = setUserModelForRequest()))
                    delay(100)
                    updateUserModelUseCase.execute(
                        userDomainModel =
                        state.value.userModel
                    )
                }

                is SettingsIntent.SetRules -> {
                    updateUserRulesUseCase.execute(rulesUserDomainModel = intent.rules)
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
