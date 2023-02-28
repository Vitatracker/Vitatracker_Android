package app.mybad.notifier.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.repos.CoursesRepo
import app.mybad.domain.repos.UserDataRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataRepo: UserDataRepo,
    private val coursesRepo: CoursesRepo
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _state = MutableStateFlow(SettingsState(
        user = userDataRepo.getUserData(),
    ))
    val state get() = _state.asStateFlow()
    init {
        scope.launch {
            _state.emit(_state.value.copy(courses = coursesRepo.getAll()))
        }
    }

    fun reduce(intent: SettingsIntent) {
        when(intent) {
            is SettingsIntent.DeleteAccount -> {}
            is SettingsIntent.Exit -> {}
            is SettingsIntent.SetNotifications -> {
                viewModelScope.launch {
                    val newUser = _state.value.user.copy(settings = _state.value.user.settings.copy(notifications = intent.notifications))
                    _state.emit(_state.value.copy(user = newUser))
                }
            }
            is SettingsIntent.ChangePassword -> { }
        }
    }
}