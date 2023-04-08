package app.mybad.notifier.ui.screens.settings

import androidx.lifecycle.ViewModel
import app.mybad.domain.repos.CoursesRepo
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
            _state.emit(_state.value.copy(personalDomainModel = userSettingsUseCase.getUserPersonal()))
            _state.emit(_state.value.copy(notificationsUserDomainModel = userSettingsUseCase.getUserNotification()))
            _state.emit(_state.value.copy(rulesUserDomainModel = userSettingsUseCase.getUserRules()))
        }
    }

    fun reduce(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.DeleteAccount -> {}
            is SettingsIntent.Exit -> {}
            is SettingsIntent.SetNotifications -> {
                scope.launch {
                    userNotificationDomainModelUseCase.execute(notificationsUserDomainModel = intent.notifications)
                    _state.emit(_state.value.copy(notificationsUserDomainModel = intent.notifications))
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
                    userRulesDomainModelUseCase.execute(rulesUserDomainModel = intent.rules)
                    _state.emit(_state.value.copy(rulesUserDomainModel = intent.rules))
                }
            }
            is SettingsIntent.ChangePassword -> {}
        }
    }
}