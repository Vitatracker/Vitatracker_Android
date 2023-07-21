package app.mybad.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import app.mybad.data.db.models.NotificationSettingsModel

@Dao
interface NotificationSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotificationSettings(notification: NotificationSettingsModel): Long?
}
