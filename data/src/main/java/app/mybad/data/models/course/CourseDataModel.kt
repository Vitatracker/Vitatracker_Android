package app.mybad.data.models.course

data class CourseDataModel(
    val id: String = "courseId",
    val userId: String = "userId",
    val medId: Long = -1L,
    val startTime: Long = -1L,
    val endTime: Long = -1L,
    val interval: Long = -1L,
    val showUsageTime: Boolean = true,
    val isFinished: Boolean = false,
)
