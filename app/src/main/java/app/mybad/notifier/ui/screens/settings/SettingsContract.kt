package app.mybad.notifier.ui.screens.settings

import app.mybad.domain.models.course.CourseDomainModel
import app.mybad.domain.models.user.NotificationsUserDomainModel
import app.mybad.domain.models.user.UserDomainModel
import kotlinx.coroutines.flow.Flow

sealed interface SettingsIntent {
    object DeleteAccount: SettingsIntent
    object Exit : SettingsIntent
    data class ChangePassword(val password: String) : SettingsIntent
    data class SetNotifications(val notifications: NotificationsUserDomainModel) : SettingsIntent
}

data class SettingsState(
    val courses: List<CourseDomainModel> = emptyList(),
    val user: UserDomainModel = UserDomainModel(id = "userid")
)