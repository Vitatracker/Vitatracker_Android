package app.mybad.domain.models.course

import app.mybad.domain.models.AuthToken

data class CourseDomainModel(
    val id: Long = 0,
    val creationDate: Long = 0,
    val updateDate: Long = 0,
    val userId: Long = AuthToken.userId,
    val comment: String = "",
    val medId: Long = -1,
    val startDate: Long = -1,
    val endDate: Long = -1,
    val remindDate: Long = -1,
    val interval: Long = -1,
    val regime: Int = 0,
    val showUsageTime: Boolean = true,
    val isFinished: Boolean = false,
    val isInfinite: Boolean = false,
)
