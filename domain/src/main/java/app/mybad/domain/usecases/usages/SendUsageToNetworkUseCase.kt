package app.mybad.domain.usecases.usages

import android.util.Log
import app.mybad.domain.models.AuthToken
import app.mybad.domain.repository.UsageRepository
import app.mybad.domain.repository.network.UsageNetworkRepository
import javax.inject.Inject

class SendUsageToNetworkUseCase @Inject constructor(
    private val usageRepository: UsageRepository,
    private val usageNetworkRepository: UsageNetworkRepository,
) {

    suspend operator fun invoke(userId: Long) = runCatching {
        if (userId != AuthToken.userId) error("error usage synchronize")
        usageRepository.getUsagesNotUpdateByUserId(userId).onSuccess { usages ->
            log("sendUsages: usages=${usages.size}")
            usages.forEach { usage ->
                if (usage.courseIdn > 0) { // только если курс уже был отправлен на сервер
                    // тут отправлять на сервер при условии, иначе удалить
                    if (usage.idn > 0 || usage.factUseTime != null) {
                        usageNetworkRepository.updateUsage(usage).onSuccess { updatedUsage ->
                            log("sendCourses: to net ok usage update=${updatedUsage.updateNetworkDate}")
                            usageRepository.updateUsage(updatedUsage).onFailure {
                                TODO("реализовать обработку ошибок")
                            }
                        }.onFailure {
                            TODO("реализовать обработку ошибок")
                        }
                    } else {
                        usageRepository.deleteUsagesById(usage.id)
                        log("deleteUsages: usage=${usage.id}")
                    }
                }
            }
        }.onFailure {
            TODO("реализовать обработку ошибок")
        }
    }

    private fun log(text: String) {
        Log.w("VTTAG", "SendUsageToNetworkUseCase::$text")
    }
}
