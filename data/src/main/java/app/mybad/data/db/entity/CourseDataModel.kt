package app.mybad.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "courses",
    foreignKeys = [
        ForeignKey(
            entity = MedDataModel::class,
            parentColumns = ["id"],
            childColumns = ["medId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ]
)
data class CourseDataModel(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val creationDate: Long = 0,
    val updateDate: Long = 0,
    val userId: Long,
    val comment: String = "",
    val medId: Long = -1L,
    val startDate: Long = -1L,
    val endDate: Long = -1L,
    val remindDate: Long = -1L,
    val interval: Long = -1L,
    val regime: Int = 0,
    val showUsageTime: Boolean = true,
    val isFinished: Boolean = false,
    val isInfinite: Boolean = false,
)
