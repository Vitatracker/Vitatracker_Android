package app.mybad.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = NotificationSettingsContract.TABLE_NAME,
)
data class NotificationSettingsModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = NotificationSettingsContract.Columns.ID)
    var id: Long = 0L,
    @ColumnInfo(name = NotificationSettingsContract.Columns.USER_ID)
    val userId: Long = 0L,
    @ColumnInfo(name = NotificationSettingsContract.Columns.IS_ENABLED)
    val isEnabled: Boolean = false,
    @ColumnInfo(name = NotificationSettingsContract.Columns.IS_FLOAT)
    val isFloat: Boolean = false,
    @ColumnInfo(name = NotificationSettingsContract.Columns.MEDICAL_CONTROL)
    val isMedicalControl: Boolean = false,
    @ColumnInfo(name = NotificationSettingsContract.Columns.NEXT_COURSE_START)
    val isNextCourseStart: Boolean = false,

    @ColumnInfo(name = NotificationSettingsContract.Columns.UPDATED_NETWORK_DATE)
    val updateNetworkDate: Long = 0L,
    @ColumnInfo(name = NotificationSettingsContract.Columns.DELETED_DATE)
    val deleteLocalDate: Long = 0,
)
