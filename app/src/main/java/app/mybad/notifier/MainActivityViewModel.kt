package app.mybad.notifier

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.user.NotificationsUserDomainModel
import app.mybad.domain.models.user.PersonalDomainModel
import app.mybad.domain.models.user.UserDomainModel
import app.mybad.domain.models.user.UserSettingsDomainModel
import app.mybad.domain.repos.UserDataRepo
import app.mybad.domain.usecases.DataStoreUseCase
import app.mybad.domain.utils.ApiResult
import app.mybad.network.models.UserModel
import app.mybad.network.repos.repo.CoursesNetworkRepo
import app.mybad.network.repos.repo.SettingsNetworkRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStore: DataStoreUseCase,

    private val coursesNetworkRepo: CoursesNetworkRepo,
    private val settingsNetworkRepo: SettingsNetworkRepo,
    private val userDataRepo: UserDataRepo
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
            AuthToken.token = dataStore.token.first()
            AuthToken.userId = dataStore.userId.first()
            AuthToken.email = dataStore.email.first()
            Log.w("VTTAG", "MainActivityViewModel::readDataStore: userId=${AuthToken.userId}")
        }
    }

    private fun observeDataStore() {
        viewModelScope.launch {
            launch {
                dataStore.token.collect {
                    Log.w("VTTAG", "MainActivityViewModel::observeDataStore: token=$it")
                    AuthToken.token = it
                    _isAuthorize.value = it.isNotBlank()
                    if (_isAuthorize.value) readData()
                }
            }
            launch {
                dataStore.userId.collect {
                    Log.w("VTTAG", "MainActivityViewModel::observeDataStore: userId=$it")
                    AuthToken.userId = it
                }
            }
            launch {
                dataStore.email.collect {
                    Log.w("VTTAG", "MainActivityViewModel::observeDataStore: email=$it")
                    AuthToken.email = it
                }
            }
        }
    }

    fun clearDataStore() {
        viewModelScope.launch {
            dataStore.clear()
        }
    }

    private suspend fun readData() {
        coursesNetworkRepo.getAll()
        getAll()
    }

    private suspend fun getAll() {
        when (val settingsResult = settingsNetworkRepo.getUserModel()) {
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

}
