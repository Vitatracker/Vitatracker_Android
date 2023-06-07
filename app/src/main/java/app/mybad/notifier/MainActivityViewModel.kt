package app.mybad.notifier

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.user.NotificationsUserDomainModel
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.models.user.UserSettingsDomainModel
import app.mybad.domain.repos.DataStoreRepo
import app.mybad.domain.repos.UserDataRepo
import app.mybad.domain.utils.ApiResult
import app.mybad.network.models.UserModel
import app.mybad.network.repos.repo.CoursesNetworkRepo
import app.mybad.network.repos.repo.SettingsNetworkRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val coursesNetworkRepo: CoursesNetworkRepo,
    private val settingsNetworkRepo: SettingsNetworkRepo,
    private val userDataRepo: UserDataRepo
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val _uiState = MutableStateFlow(MainActivityContract())
    val uiState = _uiState.asStateFlow()

    init {
        updateToken()
    }

    fun updateToken() {
        scope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    token = dataStoreRepo.getToken().first()
                )
            )
        }
        viewModelScope.launch {
            dataStoreRepo.getToken().collect {
                Log.w("MainActivity", "token: $it")
                if (it.isNotBlank()) {
                    scope.launch {
                        coursesNetworkRepo.getAll()
                        getAll()
                    }
                }
            }
        }
    }

    private suspend fun getAll() {
        val settingsResult = settingsNetworkRepo.getUserModel()
        when (settingsResult) {
            is ApiResult.ApiSuccess -> setUserModelFromBack(settingsResult.data as UserModel)
            is ApiResult.ApiError -> Log.d(
                "TAG",
                "error: ${settingsResult.code} ${settingsResult.message}"
            )

            is ApiResult.ApiException -> Log.d("TAG", "exception: ${settingsResult.e}")
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
                    isEnabled = if (userModel.notificationSettings == null) false else userModel.notificationSettings!!.isEnabled,
                    isFloat = if (userModel.notificationSettings == null) false else userModel.notificationSettings!!.isFloat,
                    medicationControl = if (userModel.notificationSettings == null) false else userModel.notificationSettings!!.medicalControl,
                    nextCourseStart = if (userModel.notificationSettings == null) false else userModel.notificationSettings!!.nextCourseStart,
                    medsId = if (userModel.notificationSettings == null) emptyList() else userModel.notificationSettings!!.id
                )
            )
        )

        userDataRepo.updateUserNotification(notification = model.settings.notifications)
        userDataRepo.updateUserPersonal(personal = model.personal)
        userDataRepo.updateUserRules(rules = model.settings.rules)
    }

    fun clearDataStore() {
        scope.launch {
            dataStoreRepo.updateToken("")
            dataStoreRepo.updateUserId("")
            updateToken()
        }
    }

}
