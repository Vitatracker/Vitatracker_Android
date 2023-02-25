package app.mybad.data.models.med

data class MedDataModel(
    val id: Long = -1L,
    val creationDate: Long = 0L,
    val updateDate: Long = 0L,
    val userId: String = "userid",
    val name: String? = null,
    val description: String? = null,
    val comment: String? = null,
    val details: MedDetailsDataModel = MedDetailsDataModel()
)