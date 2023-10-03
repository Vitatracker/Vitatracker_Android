package app.mybad.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = PatternUsageContract.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = CourseModel::class,
            parentColumns = [CourseContract.Columns.ID],
            childColumns = [PatternUsageContract.Columns.COURSE_ID],
            onDelete = ForeignKey.NO_ACTION,
        ),
    ],
    indices = [Index(PatternUsageContract.Columns.COURSE_ID)],
)
data class PatternUsageModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PatternUsageContract.Columns.ID)
    var id: Long = 0,
    @ColumnInfo(name = PatternUsageContract.Columns.IDN)
    val idn: Long = 0,

    @ColumnInfo(name = PatternUsageContract.Columns.COURSE_ID)
    val courseId: Long,
    @ColumnInfo(name = PatternUsageContract.Columns.COURSE_IDN)
    val courseIdn: Long,

    @ColumnInfo(name = PatternUsageContract.Columns.USER_ID)
    val userId: Long,
    @ColumnInfo(name = PatternUsageContract.Columns.USER_IDN)
    val userIdn: String,

    @ColumnInfo(name = PatternUsageContract.Columns.TIME_MINUTES)
    val timeInMinutes: Int, // тут только время HH:mm в минутах + 720 (12 * 60)
    @ColumnInfo(name = PatternUsageContract.Columns.QUANTITY)
    val quantity: Float = 1f,

    @ColumnInfo(name = PatternUsageContract.Columns.IS_FINISHED)
    val finished: Boolean = false,

    @ColumnInfo(name = PatternUsageContract.Columns.CREATION_DATE)
    val createdDate: Long = 0,
    @ColumnInfo(name = PatternUsageContract.Columns.UPDATED_DATE)
    val updatedDate: Long = 0,

    @ColumnInfo(name = PatternUsageContract.Columns.UPDATED_NETWORK_DATE)
    val updateNetworkDate: Long = 0,
    @ColumnInfo(name = PatternUsageContract.Columns.DELETED_DATE)
    val deleteLocalDate: Long = 0,
)
