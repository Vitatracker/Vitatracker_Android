package app.mybad.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "meds",
    foreignKeys = [
        ForeignKey(
            entity = UserLocalDataModel::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("userId")],
)
data class MedDataModel(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val creationDate: Long = 0,
    val updateDate: Long = 0,
    val userId: Long,
    val name: String? = null,
    val description: String? = null,
    val comment: String? = null,
    val type: Int = 0,
    val icon: Int = 0,
    val color: Int = 0,
    val dose: Int = 0,
    val measureUnit: Int = 0,
    val photo: String? = null,
    val beforeFood: Int = 5,
)
