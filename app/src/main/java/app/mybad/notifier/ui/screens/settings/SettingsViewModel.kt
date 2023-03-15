package app.mybad.notifier.ui.screens.settings

import androidx.lifecycle.ViewModel
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.UserDataRepo
import app.mybad.domain.usecases.settings.SwitchGlobalNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataRepo: UserDataRepo,
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
            _state.emit(_state.value.copy(personalDomainModel = userDataRepo.getUserPersonal()))
        }

        scope.launch {
            _state.emit(_state.value.copy(notificationsUserDomainModel = userDataRepo.getUserNotification()))
        }

        scope.launch {
            _state.emit(_state.value.copy(rulesUserDomainModel = userDataRepo.getUserRules()))
        }
    }

    fun reduce(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.DeleteAccount -> {}
            is SettingsIntent.Exit -> {}
            is SettingsIntent.SetNotifications -> {
                scope.launch {
                    userDataRepo.updateUserNotification(_state.last().notificationsUserDomainModel)
                }
            }
            is SettingsIntent.SetPersonal -> {
                scope.launch {
                    userDataRepo.updateUserPersonal(_state.last().personalDomainModel)
                }
            }
            is SettingsIntent.SetRules -> {
                scope.launch {
                    userDataRepo.updateUserRules(_state.last().rulesUserDomainModel)
                }
            }
            is SettingsIntent.ChangePassword -> {}
        }
    }
}