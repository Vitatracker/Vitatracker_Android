package app.mybad.domain.models.course

data class CourseDomainModel(
    val id: Long = 0L,
    val creationDate: Long = 0L,
    val updateDate: Long = 0L,
    val userId: String = "userid",
    val comment: String = "",
    val medId: Long = -1L,
    val startDate: Long = -1L,
    val endDate: Long = -1L,
    val interval: Long = -1L,
    val regime: Int = 0,
    val showUsageTime: Boolean = true,
    val isFinished: Boolean = false,
    val isInfinite: Boolean = false,
)
