package app.mybad.data.models.course

data class CourseDataModel(
    val id: String = "courseid",
    val userId: String = "userid",
    val medId: Long = -1L,
    val startTime: Long = -1L,
    val endTime: Long = -1L,
    val interval: Long = -1L,
    val showUsageTime: Boolean = true,
    val isFinished: Boolean = false,
)
