package app.mybad.domain.models

data class RemedyDomainModel(
    val id: Long = 0,
    var idn: Long = -1,

    val createdDate: Long = 0,
    val updatedDate: Long = 0,

    val userId: Long = AuthToken.userId,
    val userIdn: String = "",

    val name: String? = null,
    val description: String? = null,
    val comment: String? = null,

    val type: Int = 0,
    val icon: Int = 0,
    val color: Int = 0,
    val dose: Int = 0,
    val measureUnit: Int = 0,
    val photo: String? = null,
    val beforeFood: Int = 0,
    val notUsed: Boolean = false,

    val updateNetworkDate: Long = 0,
    val updateLocalDate: Long = 0,
)
