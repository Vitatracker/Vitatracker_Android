package app.mybad.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.mybad.data.db.models.NotificationContract
import app.mybad.data.db.models.NotificationModel

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationModel): Long

    @Query(
        "select * from ${NotificationContract.TABLE_NAME} where ${
            NotificationContract.Columns.USER_ID
        } = :userId order by ${NotificationContract.Columns.TIME} asc"
    )
    suspend fun getNotificationByUserId(userId: Long): List<NotificationModel>

    @Query(
        "delete from ${NotificationContract.TABLE_NAME} where ${
            NotificationContract.Columns.USER_ID
        } = :userId"
    )
    suspend fun deleteNotification(userId: Long)

    @Query(
        "delete from ${NotificationContract.TABLE_NAME} where ${
            NotificationContract.Columns.USER_ID
        } = :userId and ${
            NotificationContract.Columns.ID
        } = :id"
    )
    suspend fun deleteNotification(userId: Long, id: Long)

    @Query(
        "delete from ${NotificationContract.TABLE_NAME} where ${
            NotificationContract.Columns.USER_ID
        } = :userId and ${
            NotificationContract.Columns.TYPE
        } = :type and ${
            NotificationContract.Columns.TYPE_ID
        } = :typeId"
    )
    suspend fun deleteNotification(
        userId: Long,
        type: Int,
        typeId: Long
    )
}
