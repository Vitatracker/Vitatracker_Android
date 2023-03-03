package app.mybad.data.models.med

data class MedDetailsDataModel(
    val type: Int = -1,
    val icon: Int = -1,
    val dose: Int = -1,
    val measureUnit: Int = -1,
    val photo: String? = null,
    val beforeFood: Int = 5,
)
