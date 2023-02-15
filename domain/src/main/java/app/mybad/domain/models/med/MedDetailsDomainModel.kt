package app.mybad.domain.models.med

data class MedDetailsDomainModel(
    val type: Int = -1,
    val icon: Int = -1,
    val dose: Int = -1,
    val measureUnit: Int = -1,
    val photo: String? = null,
    val beforeFood: Boolean = false,
)
