package app.mybad.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = CourseContract.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = RemedyModel::class,
            parentColumns = [RemedyContract.Columns.ID],
            childColumns = [CourseContract.Columns.REMEDY_ID],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(CourseContract.Columns.REMEDY_ID)],
)
data class CourseModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = CourseContract.Columns.ID)
    var id: Long = 0,
    @ColumnInfo(name = CourseContract.Columns.IDN)
    var idn: Long = 0,
    @ColumnInfo(name = CourseContract.Columns.CREATION_DATE)
    val createdDate: Long = 0,
    @ColumnInfo(name = CourseContract.Columns.UPDATE_DATE)
    val updateDate: Long = 0,
    @ColumnInfo(name = CourseContract.Columns.USER_ID)
    val userId: Long,
    @ColumnInfo(name = CourseContract.Columns.USER_IDN)
    val userIdn: String,
    @ColumnInfo(name = CourseContract.Columns.COMMENT)
    val comment: String = "",
    @ColumnInfo(name = CourseContract.Columns.REMEDY_ID)
    val remedyId: Long = -1L,
    @ColumnInfo(name = CourseContract.Columns.START_DATE)
    val startDate: Long = -1L,
    @ColumnInfo(name = CourseContract.Columns.END_DATE)
    val endDate: Long = -1L,
    @ColumnInfo(name = CourseContract.Columns.REMIND_DATE)
    val remindDate: Long = -1L,
    @ColumnInfo(name = CourseContract.Columns.INTERVAL)
    val interval: Long = -1L,
    @ColumnInfo(name = CourseContract.Columns.REGIME)
    val regime: Int = 0,
    @ColumnInfo(name = CourseContract.Columns.SHOW_USAGE_TIME)
    val showUsageTime: Boolean = true,
    @ColumnInfo(name = CourseContract.Columns.IS_FINISHED)
    val isFinished: Boolean = false,
    @ColumnInfo(name = CourseContract.Columns.IS_INFINITE)
    val isInfinite: Boolean = false,
    @ColumnInfo(name = CourseContract.Columns.NOT_USED)
    val notUsed: Boolean = false,

    @ColumnInfo(name = CourseContract.Columns.UPDATED_NETWORK_DATE)
    val updateNetworkDate: Long = 0,
    @ColumnInfo(name = CourseContract.Columns.UPDATED_LOCAL_DATE)
    val updateLocalDate: Long = 0,
)
