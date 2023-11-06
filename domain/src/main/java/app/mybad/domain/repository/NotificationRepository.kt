package app.mybad.domain.repository

import app.mybad.domain.models.NotificationDomainModel

interface NotificationRepository {
    suspend fun getNotificationByUserId(userId: Long): Result<List<NotificationDomainModel>>
    suspend fun addNotification(notification: NotificationDomainModel): Result<Long>
    suspend fun deleteNotificationByUserId(userId: Long): Result<Unit>
    suspend fun deleteNotificationById(userId: Long, id: Long): Result<Unit>
    suspend fun deleteNotification(userId: Long, type: Int, typeId: Long): Result<Unit>
}
