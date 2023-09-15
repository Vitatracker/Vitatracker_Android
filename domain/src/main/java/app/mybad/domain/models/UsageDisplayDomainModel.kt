package app.mybad.domain.models

// для отображения данных на экране, содержит все необходимые данные
data class UsageDisplayDomainModel(
    // PatternUsage
    var id: Long = 0,
    val courseId: Long = -1,
    val userId: Long = AuthToken.userId,

    val timeInMinutes: Int = 0, // тут время HH:mm в минутах
    val quantity: Float = 1f,

    val isPattern: Boolean = true, // это для определения pattern или usage
    val sortedDate: Int = 0, // для сортировки, timeInMinutes, но с учетом часового пояса
    // для usage
    val useTime: Long = 0, // тут полная дата со временем, когда необходимо было принять
    val factUseTime: Long = -1, // дата фактического приема время с учетом часового пояса

    // из курса для информации
    val courseIdn: Long = 0,

    val remedyId: Long = -1,

    val startDate: Long = 0,
    val endDate: Long = 0,
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
    fun toUsageKey() = "%04d%06d%06d".format(sortedDate, courseId, remedyId)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UsageDisplayDomainModel

        if (courseId != other.courseId) return false
        if (factUseTime != other.factUseTime) return false
        if (timeInMinutes != other.timeInMinutes) return false
        if (useTime != other.useTime) return false
        if (remedyId != other.remedyId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = courseId.hashCode()
        result = 31 * result + factUseTime.hashCode()
        result = 31 * result + timeInMinutes
        result = 31 * result + useTime.hashCode()
        result = 31 * result + remedyId.hashCode()
        return result
    }
}
