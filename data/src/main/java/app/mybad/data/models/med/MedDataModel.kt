package app.mybad.data.models.med

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meds")
data class MedDataModel(
    @PrimaryKey var id: Long = 0L,
    val creationDate: Long = 0L,
    val updateDate: Long = 0L,
    val userId: Long = 0L,
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
