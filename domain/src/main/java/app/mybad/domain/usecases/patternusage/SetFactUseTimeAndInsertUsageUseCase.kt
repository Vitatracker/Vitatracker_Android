package app.mybad.domain.usecases.patternusage

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.repository.PatternUsageRepository
import app.mybad.domain.usecases.usages.CheckUseUsagesUseCase
import app.mybad.domain.usecases.usages.UpdateUsageUseCase
import app.mybad.utils.currentDateTimeSystem
import app.mybad.utils.displayDateTime
import app.mybad.utils.displayTimeInMinutes
import app.mybad.utils.toDateTimeSystem
import javax.inject.Inject

class SetFactUseTimeAndInsertUsageUseCase @Inject constructor(
    private val patternUsageRepository: PatternUsageRepository,

    private val updateUsageUseCase: UpdateUsageUseCase,
    private val checkUseUsagesUseCase: CheckUseUsagesUseCase,
) {

    suspend operator fun invoke(patternId: Long, useTime: Long) {
        Log.w(
            "VTTAG",
            "SetFactUseTimeAndInsertUsageUseCase:: check patternId=$patternId useTime=${
                if (useTime > 0) useTime.toDateTimeSystem().displayDateTime() else 0
            }"
        )
        if (patternId <= 0 && useTime <= 0) return
        try {
            patternUsageRepository.getPatternUsageById(patternId).getOrNull()?.let { pattern ->
                if (pattern.userId != AuthToken.userId) error("Error: corrupted user!")
                val useTimeSystem = useTime.toDateTimeSystem()
                if (!checkUseUsagesUseCase(pattern.courseId, useTimeSystem)) {
                    updateUsageUseCase( // добавить новый usage из паттерна
                        UsageDomainModel(
                            id = 0,
                            idn = 0,
                            userId = pattern.userId,
                            userIdn = "",
                            courseId = pattern.courseId,
                            courseIdn = pattern.courseIdn,

                            useTime = useTimeSystem, // время когда нужно было принять с учетом часового пояса
                            factUseTime = currentDateTimeSystem(), // фактическое время приема с учетом часового пояса
                            quantity = pattern.quantity,
                        )
                    ).onSuccess {
                        Log.w(
                            "VTTAG",
                            "SetFactUseTimeAndInsertUsageUseCase:: use patternId=$patternId useTime=${
                                useTime.toDateTimeSystem().displayDateTime()
                            } (${pattern.timeInMinutes.displayTimeInMinutes()})"
                        )
                        // Синхронизируем с сервером, но тут с задержкой, если много раз чекать
                        AuthToken.requiredUsagesSynchronize()
                    }
                }
            }
        } catch (e: Error) {

        }
    }
}
