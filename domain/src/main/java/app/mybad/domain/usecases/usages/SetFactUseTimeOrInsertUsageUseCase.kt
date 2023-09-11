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
    ) = runCatching {
        if (AuthToken.userId != usageDisplay.userId) error("Error: corrupted user!")
        val factUseTime = if (usageDisplay.factUseTime > 0) -1L else currentDateTime
        val usageId = if (usageDisplay.isPattern) {
            Log.d(
                "VTTAG",
                "SetFactUseTimeOrInsertUsageUseCase::insert: id=${usageDisplay.id} - $factUseTime"
            )
            // добавить новый usage из паттерна
            repository.insertUsage(
                UsageDomainModel(
                    id = 0,
                    idn = 0,
                    userId = usageDisplay.userId,
                    userIdn = "",
                    courseId = usageDisplay.courseId,
                    courseIdn = 0,

                    createdDate = currentDateTime,
                    updatedDate = 0,

                    useTime = usageDisplay.useTime, // тут время когда нужно было принять
                    factUseTime = factUseTime, // тут фактическое время приема
                    quantity = usageDisplay.quantity,
                )
            ).getOrThrow()
        } else {
            // обновим чек
            repository.setFactUseTimeUsage(usageDisplay.id, factUseTime).getOrThrow()
            usageDisplay.id
        }
        Log.w(
            "VTTAG",
            "SetFactUseTimeOrInsertUsageUseCase: usageId=$usageId - $factUseTime"
        )
        // Синхронизируем с сервером, но тут с задержкой, если много раз чекать
        AuthToken.requiredSetUsagesFactTime(usageId)
    }
}
