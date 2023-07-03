package app.mybad.domain.models.usages

import app.mybad.domain.models.AuthToken

data class UsageCommonDomainModel(
    val id: Long = 0,
    val medId: Long = -1,
    val userId: Long = AuthToken.userId,
    val creationTime: Long = 0L,
    val editTime: Long = 0L,
    val useTime: Long = 0L,
    val factUseTime: Long = -1L,
    val quantity: Int = 1,
    val isDeleted: Boolean = false
)
