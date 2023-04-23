package app.mybad.domain.models.usages

data class UsageCommonDomainModel(
    val id: Int = 0,
    val medId: Long,
    val userId: String,
    val creationTime: Long = 0L,
    val editTime: Long = 0L,
    val useTime: Long,
    val factUseTime: Long = -1L,
    val quantity: Int = 1,
    val isDeleted: Boolean = false
)
