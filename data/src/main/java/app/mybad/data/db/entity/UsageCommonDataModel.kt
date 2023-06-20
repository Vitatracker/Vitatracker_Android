package app.mybad.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "usages_common",
    foreignKeys = [
        ForeignKey(
            entity = UserLocalDataModel::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class UsageCommonDataModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val medId: Long,
    val userId: Long,
    val creationTime: Long = 0,
    val editTime: Long = 0,
    val useTime: Long,
    val factUseTime: Long = -1L,
    val quantity: Int = 1,
    val isDeleted: Boolean = false
)
