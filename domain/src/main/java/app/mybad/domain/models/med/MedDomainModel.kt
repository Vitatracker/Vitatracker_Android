package app.mybad.domain.models.med

data class MedDomainModel(
    val id: String = "medid",
    val userId: String = "userid",
    val name: String? = null,
    val description: String? = null,
    val comment: String? = null,
    val details: MedDetailsDomainModel = MedDetailsDomainModel()
)