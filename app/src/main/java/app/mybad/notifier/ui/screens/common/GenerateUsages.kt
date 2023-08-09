package app.mybad.notifier.ui.screens.common

import android.util.Log
import app.mybad.data.models.UsageFormat
import app.mybad.domain.models.UsageDomainModel
import app.mybad.utils.changeTime
import app.mybad.utils.daysBetween
import app.mybad.utils.currentDateTime
import app.mybad.utils.plusDays
import app.mybad.utils.toEpochSecond
import app.mybad.utils.toLocalDateTime

fun generateUsages(
    usagesByDay: List<UsageFormat>,
    remedyId: Long,
    courseId: Long,
    userId: Long,
    startDate: Long,
    endDate: Long,
    regime: Int,
): List<UsageDomainModel> {
    val now = currentDateTime().toEpochSecond()
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
                    .changeTime(minute = time)
                    .plusDays(position)
                    .toEpochSecond()
                Log.w(
                    "VTTAG",
                    "generateUsages: useTime=${useTime.toLocalDateTime()}"
                )
                if (useTime > now) {
                    usage.add(
                        UsageDomainModel(
                            remedyId = remedyId,
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
