package app.mybad.data.models.med

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meds")
data class MedDataModel(
    @PrimaryKey var id: Long = 0L,
    val creationDate: Long = 0L,
    val updateDate: Long = 0L,
    val userId: String = "userid",
    val name: String? = null,
    val description: String? = null,
    val comment: String? = null,
    val details: MedDetailsDataModel = MedDetailsDataModel()
)