package app.mybad.notifier.ui.screens.common

import android.util.Log
import app.mybad.domain.models.UsageDomainModel
import app.mybad.theme.utils.changeTime
import app.mybad.theme.utils.daysBetween
import app.mybad.theme.utils.getCurrentDateTime
import app.mybad.theme.utils.plusDays
import app.mybad.theme.utils.toEpochSecond
import app.mybad.theme.utils.toLocalDateTime

fun generateUsages(
    usagesByDay: List<Pair<Long, Int>>,
    courseId: Long,
    userId: Long,
    startDate: Long,
    endDate: Long,
    regime: Int,
): List<UsageDomainModel> {
    val now = getCurrentDateTime().toEpochSecond()
    val interval = endDate.daysBetween(startDate).toInt()
    Log.w(
        "VTTAG",
        "generateUsages: interval=$interval startDate=${startDate.toLocalDateTime()} endDate=${endDate.toLocalDateTime()}"
    )
    val usage = mutableListOf<UsageDomainModel>()
    Log.w(
        "VTTAG",
        "generateUsages: userId=${userId} interval=${interval} regime=$regime usagesByDay=${usagesByDay}"
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
                    "generateUsages: useTime=${useTime.toLocalDateTime()} usage=${now.toLocalDateTime()}"
                )
                if (useTime > now) {
                    usage.add(
                        UsageDomainModel(
                            courseId = courseId,
                            userId = userId,
                            createdDate = now,
                            useTime = useTime,
                            quantity = quantity,
                        )
                    )
                }
            }
        }
    }
    Log.w("VTTAG", "generateUsages: size=${usage.size} usage=${usage}")
    return usage.toList()
}
