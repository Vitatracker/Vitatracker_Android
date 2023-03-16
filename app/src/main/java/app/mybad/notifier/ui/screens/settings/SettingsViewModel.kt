package app.mybad.notifier.ui.screens.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.UserDataRepo
import app.mybad.domain.usecases.settings.SwitchGlobalNotificationsUseCase
import app.mybad.domain.usecases.user.LoadUserSettingsUseCase
import app.mybad.domain.usecases.user.UpdateUserNotificationDomainModelUseCase
import app.mybad.domain.usecases.user.UpdateUserPersonalDomainModelUseCase
import app.mybad.domain.usecases.user.UpdateUserRulesDomainModelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userSettingsUseCase: LoadUserSettingsUseCase,
    private val userRulesDomainModelUseCase: UpdateUserRulesDomainModelUseCase,
    private val userPersonalDomainModelUseCase: UpdateUserPersonalDomainModelUseCase,
    private val userNotificationDomainModelUseCase: UpdateUserNotificationDomainModelUseCase,
    private val coursesRepo: CoursesRepo,
    private val switchGlobalNotificationsUseCase: SwitchGlobalNotificationsUseCase,
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _state = MutableStateFlow(SettingsState())
    val state get() = _state.asStateFlow()

    init {
        scope.launch {
            _state.emit(_state.value.copy(courses = coursesRepo.getAll()))
        }

        scope.launch {
            _state.emit(_state.value.copy(personalDomainModel = userSettingsUseCase.getUserPersonal()))
        }

        scope.launch {
            _state.emit(_state.value.copy(notificationsUserDomainModel = userSettingsUseCase.getUserNotification()))
        }

        scope.launch {
            _state.emit(_state.value.copy(rulesUserDomainModel = userSettingsUseCase.getUserRules()))
        }
    }

    fun reduce(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.DeleteAccount -> {}
            is SettingsIntent.Exit -> {}
            is SettingsIntent.SetNotifications -> {
                scope.launch {
                    userNotificationDomainModelUseCase.execute(_state.last().notificationsUserDomainModel)
                }
            }
            is SettingsIntent.SetPersonal -> {
                scope.launch {
                    userPersonalDomainModelUseCase.execute(intent.personal)
                    _state.emit(_state.value.copy(personalDomainModel = intent.personal))
                }
            }
            is SettingsIntent.SetRules -> {
                scope.launch {
                    userRulesDomainModelUseCase.execute(_state.last().rulesUserDomainModel)
                }
            }
            is SettingsIntent.ChangePassword -> {}
        }
    }
}