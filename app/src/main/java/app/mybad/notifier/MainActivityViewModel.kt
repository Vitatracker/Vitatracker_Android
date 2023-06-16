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
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStore: DataStoreUseCase,

    private val coursesNetworkRepo: CoursesNetworkRepo,
    private val settingsNetworkRepo: SettingsNetworkRepo,
    private val userDataRepo: UserDataRepo
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val isAuthorize = dataStore.token.mapLatest {
        Log.w("VTTAG", "MainActivityViewModel: isAuthorize=${it.isNotBlank()}")

        it.isNotBlank()
    }

    init {
        Log.w("VTTAG", "MainActivityViewModel::init: app start")
        observeDataStore()
    }

    private fun observeDataStore() {
        viewModelScope.launch {
            dataStore.token.collect {
                Log.w("VTTAG", "MainActivityViewModel::readDataStore: token=$it")
                AuthToken.token = it
                if (AuthToken.token.isNotBlank()) readData()
            }
            dataStore.userId.collect {
                Log.w("VTTAG", "MainActivityViewModel::readDataStore: userId=$it")
                AuthToken.userId = it
            }
            dataStore.mail.collect {
                Log.w("VTTAG", "MainActivityViewModel::readDataStore: email=$it")
                AuthToken.email = it
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
        val settingsResult = settingsNetworkRepo.getUserModel()
        when (settingsResult) {
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
