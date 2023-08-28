package app.mybad.data.models

import app.mybad.utils.minusMonths
import app.mybad.utils.plusMonths

data class DateCourseLimit(
    val currentDate: Long = 0L,
) {
    val minStartDate by lazy {
        currentDate.minusMonths(12)
    }
    val maxStartDate by lazy {
        currentDate.plusMonths(6)
    }
    val maxEndDate by lazy {
        currentDate.plusMonths(60)
    }
}
