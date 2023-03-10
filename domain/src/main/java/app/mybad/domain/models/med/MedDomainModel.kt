package app.mybad.domain.models.med

data class MedDomainModel(
    val id: Long = 0L,
    val creationDate: Long = 0L,
    val updateDate: Long = 0L,
    val userId: String = "userid",
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