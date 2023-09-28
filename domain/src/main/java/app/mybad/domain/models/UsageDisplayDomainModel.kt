package app.mybad.domain.models

import app.mybad.utils.betweenDays
import app.mybad.utils.isBetweenDay
import app.mybad.utils.isEqualsDay
import app.mybad.utils.notNullDateTime
import app.mybad.utils.systemToEpochSecond
import kotlinx.datetime.LocalDateTime

// для отображения данных на экране, содержит все необходимые данные
data class UsageDisplayDomainModel(
    // PatternUsage
    var id: Long = 0,
    val courseId: Long = -1,
    val userId: Long = AuthToken.userId,

    val timeInMinutes: Int = 0, // тут время HH:mm в минутах  с учетом часового пояса
    val quantity: Float = 1f,

    val isPattern: Boolean = true, // это для определения pattern или usage
    // для usage
    val useTime: LocalDateTime = notNullDateTime, // тут полная дата со временем, когда необходимо было принять с учетом часового пояса
    val factUseTime: LocalDateTime? = null, // дата фактического приема время с учетом часового пояса

    // из курса для информации
    val courseIdn: Long = 0,

    val remedyId: Long = -1,

    val startDate: LocalDateTime = notNullDateTime, // с учетом часового пояса
    val endDate: LocalDateTime = notNullDateTime, // с учетом часового пояса
    val isInfinite: Boolean = false,

    val regime: Int = 0,

    val showUsageTime: Boolean = true,

    val isFinished: Boolean = false,
    val notUsed: Boolean = false,

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
) {
    fun toUsageKey() = "%011d%06d%06d".format(useTime.systemToEpochSecond(), courseId, remedyId)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UsageDisplayDomainModel

        if (courseId != other.courseId) return false
        if (timeInMinutes != other.timeInMinutes) return false
        if (useTime != other.useTime) return false
        if (factUseTime != other.factUseTime) return false
        if (remedyId != other.remedyId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = courseId.hashCode()
        result = 31 * result + timeInMinutes
        result = 31 * result + useTime.hashCode()
        result = 31 * result + (factUseTime?.hashCode() ?: 0)
        result = 31 * result + remedyId.hashCode()
        return result
    }
}

fun UsageDisplayDomainModel.checkDate(date: LocalDateTime): Boolean {
    return if (this.isPattern) {
        date.isEqualsDay(startDate) || (date.isBetweenDay(this.startDate, this.endDate) &&
            (this.regime == 0 || date.betweenDays(this.startDate) % (this.regime + 1) == 0L))
    } else {
        this.useTime.isEqualsDay(date)
    }
}
