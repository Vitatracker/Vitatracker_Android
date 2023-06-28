package app.mybad.notifier.ui.screens.common

import android.util.Log
import app.mybad.domain.models.usages.UsageCommonDomainModel
import app.mybad.notifier.utils.changeTime
import app.mybad.notifier.utils.daysBetween
import app.mybad.notifier.utils.getCurrentDateTime
import app.mybad.notifier.utils.plusDays
import app.mybad.notifier.utils.toEpochSecond
import app.mybad.notifier.utils.toLocalDateTime

fun generateCommonUsages(
    usagesByDay: List<Pair<Long, Int>>,
    medId: Long,
    userId: Long,
    startDate: Long,
    endDate: Long,
    regime: Int,
): List<UsageCommonDomainModel> {
    val now = getCurrentDateTime().toEpochSecond()
    val interval = endDate.daysBetween(startDate).toInt()
    Log.w(
        "VTTAG",
        "generateCommonUsages: interval=$interval startDate=${startDate.toLocalDateTime()} endDate=${endDate.toLocalDateTime()}"
    )
    val usage = mutableListOf<UsageCommonDomainModel>()
    Log.w(
        "VTTAG",
        "generateCommonUsages: userId=${userId} interval=${interval} regime=$regime usagesByDay=${usagesByDay}"
    )
    if (interval <= 0 || usagesByDay.isEmpty()) return emptyList()
    repeat(interval) { position ->
        if (position % (regime + 1) == 0) {
            usagesByDay.forEach { (time, quantity) ->
                val useTime = startDate.toLocalDateTime()
                    .changeTime(time)
                    .plusDays(position)
                    .toEpochSecond()
                Log.w(
                    "VTTAG",
                    "generateCommonUsages: useTime=${useTime.toLocalDateTime()} usage=${now.toLocalDateTime()}"
                )
                if (useTime > now) {
                    usage.add(
                        UsageCommonDomainModel(
                            medId = medId,
                            userId = userId,
                            creationTime = now,
                            useTime = useTime,
                            quantity = quantity,
                        )
                    )
                }
            }
        }
    }
    Log.w("VTTAG", "generateCommonUsages: size=${usage.size} usage=${usage}")
    return usage.toList()
}
