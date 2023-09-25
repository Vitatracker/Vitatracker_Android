package app.mybad.domain.usecases.usages

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.repository.UsageRepository
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class SetFactUseTimeOrInsertUsageUseCase @Inject constructor(
    private val repository: UsageRepository,
) {

    suspend operator fun invoke(
        usageDisplay: UsageDisplayDomainModel,
        currentDateTime: LocalDateTime, // локал
        currentDateTimeUTC: Long, // UTC
        useTimeUTC: Long, // UTC
    ) = runCatching {
        if (AuthToken.userId != usageDisplay.userId) error("Error: corrupted user!")
        val factUseTime = if (usageDisplay.factUseTime == null) currentDateTime else null
        val factUseTimeUTC = if (usageDisplay.factUseTime == null) currentDateTimeUTC else null
        val usageId = if (usageDisplay.isPattern) {
            Log.d(
                "VTTAG",
                "SetFactUseTimeOrInsertUsageUseCase:CalendarViewModel:insert: id=${usageDisplay.id} - $factUseTime"
            )
            // проверить по параметрам не было ли такого, нужен id и factUseTime
            repository.getUsageByParams(
                userId = usageDisplay.userId,
                courseId = usageDisplay.courseId,
                useTime = useTimeUTC,
            ).getOrNull()?.let { usage ->
                Log.d(
                    "VTTAG",
                    "SetFactUseTimeOrInsertUsageUseCase:CalendarViewModel:update: id=${usage.id}: $factUseTime"
                )
                // обновим чек, ставим тут, то что пришло с экрана
                repository.setFactUseTimeUsage(usageId = usage.id, factUseTime = factUseTimeUTC)
                    .getOrThrow()
            } ?: repository.insertUsage( // добавить новый usage из паттерна
                UsageDomainModel(
                    id = 0,
                    idn = 0,
                    userId = usageDisplay.userId,
                    userIdn = "",
                    courseId = usageDisplay.courseId,
                    courseIdn = usageDisplay.courseIdn,

                    useTime = usageDisplay.useTime, // тут время когда нужно было принять с учетом часового пояса
                    factUseTime = factUseTime, // тут фактическое время приема с учетом часового пояса
                    quantity = usageDisplay.quantity,
                )
            ).getOrThrow()
        } else {
            Log.w(
                "VTTAG",
                "SetFactUseTimeOrInsertUsageUseCase:CalendarViewModel: usageId=${usageDisplay.id} - $factUseTimeUTC"
            )
            // обновим чек
            repository.setFactUseTimeUsage(usageId = usageDisplay.id, factUseTime = factUseTimeUTC)
                .getOrThrow()
        }
        Log.w(
            "VTTAG",
            "SetFactUseTimeOrInsertUsageUseCase:CalendarViewModel: usageId=$usageId - $factUseTime - $factUseTimeUTC"
        )
        // Синхронизируем с сервером, но тут с задержкой, если много раз чекать
        AuthToken.requiredSetUsagesFactTime(usageId)
    }
}
