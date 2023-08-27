package app.mybad.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = UsageContract.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = CourseModel::class,
            parentColumns = [CourseContract.Columns.ID],
            childColumns = [UsageContract.Columns.COURSE_ID],
            onDelete = ForeignKey.NO_ACTION,
        ),
    ],
    indices = [Index(UsageContract.Columns.USER_ID), Index(UsageContract.Columns.COURSE_ID)],
)
data class UsageModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = UsageContract.Columns.ID)
    var id: Long = 0,
    @ColumnInfo(name = UsageContract.Columns.IDN)
    val idn: Long = 0,

    @ColumnInfo(name = UsageContract.Columns.COURSE_ID)
    val courseId: Long,
    @ColumnInfo(name = UsageContract.Columns.COURSE_IDN)
    val courseIdn: Long,

    @ColumnInfo(name = UsageContract.Columns.REMEDY_ID)
    val remedyId: Long,
    @ColumnInfo(name = UsageContract.Columns.REMEDY_IDN)
    val remedyIdn: Long,

    @ColumnInfo(name = UsageContract.Columns.USER_ID)
    val userId: Long,
    @ColumnInfo(name = UsageContract.Columns.USER_IDN)
    val userIdn: String,

    @ColumnInfo(name = UsageContract.Columns.CREATION_DATE)
    val creationDate: Long = 0,
    @ColumnInfo(name = UsageContract.Columns.UPDATED_DATE)
    val updatedDate: Long = 0,

    @ColumnInfo(name = UsageContract.Columns.USE_TIME)
    val useTime: Long, // тут полная дата со временем, но нужно только время
    @ColumnInfo(name = UsageContract.Columns.FACT_USE_TIME)
    val factUseTime: Long = 0,

    @ColumnInfo(name = UsageContract.Columns.QUANTITY)
    val quantity: Int = 1,
    @ColumnInfo(name = UsageContract.Columns.IS_DELETED)
    val isDeleted: Boolean = false,
    @ColumnInfo(name = UsageContract.Columns.NOT_USED)
    val notUsed: Boolean = false,

    @ColumnInfo(name = UsageContract.Columns.UPDATED_NETWORK_DATE)
    val updateNetworkDate: Long = 0,
    @ColumnInfo(name = UsageContract.Columns.UPDATED_LOCAL_DATE)
    val updateLocalDate: Long = 0,
    @ColumnInfo(name = UsageContract.Columns.DELETED_DATE)
    val deleteLocalDate: Long = 0,
)
