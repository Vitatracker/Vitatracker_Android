package app.mybad.data.models.course

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseDataModel(
    @PrimaryKey(autoGenerate = true) val id: Long = -1L,
    val creationDate: Long = 0L,
    val updateDate: Long = 0L,
    val userId: String = "userid",
    val comment: String = "",
    val medId: Long = -1L,
    val startDate: Long = -1L,
    val endDate: Long = -1L,
    val interval: Long = -1L,
    val showUsageTime: Boolean = true,
    val isFinished: Boolean = false,
    val isInfinite: Boolean = false,
)
