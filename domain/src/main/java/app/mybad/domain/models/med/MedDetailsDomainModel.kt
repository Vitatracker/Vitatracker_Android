package app.mybad.domain.models.med

data class MedDetailsDomainModel(
    val type: Int = 0,
    val icon: Int = 0,
    val dose: Int = 0,
    val measureUnit: Int = 0,
    val photo: String? = null,
    val beforeFood: Int = 5,
)
