package app.mybad.domain.models

import app.mybad.utils.notNullDateTime
import kotlinx.datetime.LocalDateTime

data class UsageDomainModel(
    val id: Long = 0,
    val idn: Long = 0,

    val userId: Long = AuthToken.userId,
    val userIdn: String = "",

    val courseId: Long = -1,
    val courseIdn: Long = 0,

    val createdDate: Long = 0,
    val updatedDate: Long = 0,

    val factUseTime: LocalDateTime? = null,
    val useTime: LocalDateTime = notNullDateTime, // тут полная дата со временем
    val quantity: Float = 1f,

    val isDeleted: Boolean = false,
    val notUsed: Boolean = false,

    val updateNetworkDate: Long = 0,
)
