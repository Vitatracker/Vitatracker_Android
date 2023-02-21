package app.mybad.domain.models.course

data class CourseDomainModel(
    val id: String = "courseid",
    val userId: String = "userid",
    val comment: String = "",
    val medId: Long = -1L,
    val startDate: Long = -1L,
    val endTime: Long = -1L,
    val interval: Long = -1L,
    val showUsageTime: Boolean = true,
    val isFinished: Boolean = false,
)
