package app.mybad.data.models.usages

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usages")
data class UsagesDataModel(
    @PrimaryKey var medId: Long = 0L,
    val creationDate: Long = 0L,
    val userId: String = "userid",
    val needControl: Boolean = false,
    val usages: List<UsageDataModel> = emptyList()
)
