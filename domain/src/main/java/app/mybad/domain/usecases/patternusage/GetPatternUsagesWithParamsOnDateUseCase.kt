package app.mybad.domain.usecases.patternusage

import app.mybad.domain.repository.PatternUsageRepository
import app.mybad.domain.usecases.usages.CheckUseUsagesUseCase
import app.mybad.utils.atEndOfDay
import app.mybad.utils.systemToEpochSecond
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class GetPatternUsagesWithParamsOnDateUseCase @Inject constructor(
    private val repository: PatternUsageRepository,
    private val checkUseUsagesUseCase: CheckUseUsagesUseCase,
) {

    suspend operator fun invoke(
        userId: Long,
        date: LocalDateTime,
    ) = repository.getPatternUsagesWithParamsOnDate(
        userId = userId,
        startTime = date.systemToEpochSecond(),
        endTime = date.atEndOfDay().systemToEpochSecond()
    ).getOrNull()?.mapNotNull { usage ->
        if (usage.withinDayOnRegime(date) &&
            !checkUseUsagesUseCase(usage.courseId, usage.useTime)
        ) usage else null
    }
}
