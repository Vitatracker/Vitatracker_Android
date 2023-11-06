package app.mybad.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = NotificationContract.TABLE_NAME,
)
data class NotificationModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = NotificationContract.Columns.ID)
    var id: Long = 0L,
    @ColumnInfo(name = NotificationContract.Columns.USER_ID)
    val userId: Long = 0L,
    @ColumnInfo(name = NotificationContract.Columns.IS_ENABLED)
    val isEnabled: Boolean = true,
    @ColumnInfo(name = NotificationContract.Columns.TYPE)
    val type: Int = 0,
    @ColumnInfo(name = NotificationContract.Columns.TYPE_ID)
    val typeId: Long = 0,
    @ColumnInfo(name = NotificationContract.Columns.DATE)
    val date: String = "",
    @ColumnInfo(name = NotificationContract.Columns.TIME)
    val time: Long = 0,
)
