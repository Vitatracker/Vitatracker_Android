package app.mybad.data.models.med

data class MedDataModel(
    val id: String = "medId",
    val userId: String = "userId",
    val name: String? = null,
    val description: String? = null,
    val comment: String? = null,
    val details: MedDetailsDataModel = MedDetailsDataModel()
)