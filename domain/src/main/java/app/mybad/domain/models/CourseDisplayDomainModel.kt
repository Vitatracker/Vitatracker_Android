package app.mybad.domain.models

import app.mybad.utils.notNullDateTime
import kotlinx.datetime.LocalDateTime

data class CourseDisplayDomainModel(
    val id: Long = 0,
    var idn: Long = 0,

    val userId: Long = AuthToken.userId,
    val userIdn: String = "",

    val remedyId: Long = -1,
    val remedyIdn: Long = 0,

    val startDate: LocalDateTime = notNullDateTime,
    val endDate: LocalDateTime = notNullDateTime,

    val regime: Int = 0,
    val isFinished: Boolean = false,
    val isInfinite: Boolean = false,
    val notUsed: Boolean = false,

    val patternUsages: String = "",

    val comment: String = "",

    val remindDate: LocalDateTime? = null,
    val interval: Long = 0,

    val createdDate: Long = 0,
    val updatedDate: Long = 0,

    val updateNetworkDate: Long = 0,

    // из ремеди для отображения
    val name: String = "",
    val description: String = "",

    val type: Int = 0,
    val icon: Int = 0,
    val color: Int = 0,
    val dose: Int = 0,
    val beforeFood: Int = 5,

    val measureUnit: Int = 0,
    val photo: String? = null,
)
