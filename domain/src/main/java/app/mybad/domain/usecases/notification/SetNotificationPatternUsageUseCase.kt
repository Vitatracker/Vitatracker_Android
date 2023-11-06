package app.mybad.domain.usecases.notification

import app.mybad.domain.usecases.patternusage.GetPatternUsageIdUseCase
import javax.inject.Inject

class SetNotificationPatternUsageUseCase @Inject constructor(
    private val getPatternUsageIdUseCase: GetPatternUsageIdUseCase,
    private val addNotificationsByPatternIdUseCase: AddNotificationsByPatternIdUseCase,
    private val cancelNotificationsByPatternIdUseCase: CancelNotificationsByPatternIdUseCase,
) {
    suspend operator fun invoke(
        userId: Long,
        courseId: Long,
        patternId: Long?,
        timeInMinutes: Int,
        isNotification: Boolean
    ) {
        (patternId ?: getPatternUsageIdUseCase(userId, courseId, timeInMinutes))?.let {
            if (isNotification) cancelNotificationsByPatternIdUseCase(it)
            else addNotificationsByPatternIdUseCase(it)
        }
    }
}
