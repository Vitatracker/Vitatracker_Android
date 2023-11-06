package app.mybad.data.repos

import app.mybad.data.db.dao.NotificationDao
import app.mybad.data.mapToData
import app.mybad.data.mapToDomain
import app.mybad.domain.models.NotificationDomainModel
import app.mybad.domain.repository.NotificationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class NotificationRepositoryImpl @Inject constructor(
    private val db: NotificationDao,
    @Named("IoDispatcher") private val dispatcher: CoroutineDispatcher,
) : NotificationRepository {
    override suspend fun getNotificationByUserId(userId: Long) = withContext(dispatcher) {
        runCatching {
            db.getNotificationByUserId(userId).mapToDomain()
        }
    }

    override suspend fun addNotification(notification: NotificationDomainModel) =
        withContext(dispatcher) {
            runCatching {
                db.insertNotification(notification.mapToData())
            }
        }

    override suspend fun deleteNotificationByUserId(userId: Long) = withContext(dispatcher) {
        runCatching {
            db.deleteNotification(userId)
        }
    }

    override suspend fun deleteNotificationById(userId: Long, id: Long) = withContext(dispatcher) {
        runCatching {
            db.deleteNotification(userId = userId, id = id)
        }
    }

    override suspend fun deleteNotification(
        userId: Long,
        type: Int,
        typeId: Long
    ) = withContext(dispatcher) {
        runCatching {
            db.deleteNotification(userId = userId, type = type, typeId = typeId)
        }
    }
}
