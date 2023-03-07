package app.mybad.data.models.usages

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usages_common")
data class UsageCommonDataModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val medId: Long,
    val userId: String,
    val creationTime: Long = 0L,
    val editTime: Long = 0L,
    val useTime: Long,
    val factUseTime: Long = -1L,
    val quantity: Int = 1,
)