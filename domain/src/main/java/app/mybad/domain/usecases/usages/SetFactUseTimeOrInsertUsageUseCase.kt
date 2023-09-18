package app.mybad.domain.usecases.usages

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.models.UsageDisplayDomainModel
import app.mybad.domain.models.UsageDomainModel
import app.mybad.domain.repository.UsageRepository
import javax.inject.Inject

class SetFactUseTimeOrInsertUsageUseCase @Inject constructor(
    private val repository: UsageRepository,
) {

    suspend operator fun invoke(
        usageDisplay: UsageDisplayDomainModel,
        currentDateTime: Long,
        useTime: Long = 0, // передается из календаря
    ) = runCatching {
        if (AuthToken.userId != usageDisplay.userId) error("Error: corrupted user!")
        val factUseTime = if (usageDisplay.factUseTime > 0) -1L else currentDateTime
        val usageId = if (usageDisplay.isPattern) {
            Log.d(
                "VTTAG",
                "SetFactUseTimeOrInsertUsageUseCase:CalendarViewModel:insert: id=${usageDisplay.id} - $factUseTime"
            )
            // проверить по параметрам не было ли такого, нужен id и factUseTime
            repository.getUsageByParams(
                userId = usageDisplay.userId,
                courseId = usageDisplay.courseId,
                useTime = usageDisplay.useTime,
            ).getOrNull()?.let { usage ->
                Log.d(
                    "VTTAG",
                    "SetFactUseTimeOrInsertUsageUseCase:CalendarViewModel:update: id=${usage.id}: ${factUseTime}"
                )
                // обновим чек, ставим тут, то что пришло с экрана
                repository.setFactUseTimeUsage(usageId = usage.id, factUseTime = factUseTime)
                    .getOrThrow()
            } ?: repository.insertUsage( // добавить новый usage из паттерна
                UsageDomainModel(
                    id = 0,
                    idn = 0,
                    userId = usageDisplay.userId,
                    userIdn = "",
                    courseId = usageDisplay.courseId,
                    courseIdn = usageDisplay.courseIdn,

                    createdDate = currentDateTime,
                    updatedDate = 0,

                    useTime = usageDisplay.useTime, // тут время когда нужно было принять
                    factUseTime = factUseTime, // тут фактическое время приема
                    quantity = usageDisplay.quantity,
                )
            ).getOrThrow()
        } else {
            // обновим чек
            repository.setFactUseTimeUsage(usageId = usageDisplay.id, factUseTime = factUseTime)
                .getOrThrow()
        }
        Log.w(
            "VTTAG",
            "SetFactUseTimeOrInsertUsageUseCase:CalendarViewModel: usageId=$usageId - $factUseTime"
        )
        // Синхронизируем с сервером, но тут с задержкой, если много раз чекать
        AuthToken.requiredSetUsagesFactTime(usageId)
    }
}
