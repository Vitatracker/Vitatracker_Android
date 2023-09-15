package app.mybad.data.models

import app.mybad.utils.MINUTES_IN_DAY
import app.mybad.utils.changeTime
import app.mybad.utils.changeTimeToSystemTimeInMinutes
import app.mybad.utils.currentDateTimeInSecond
import app.mybad.utils.currentTimeInMinutes
import app.mybad.utils.timeInMinutesToDisplay

data class UsageFormat(
    val timeInMinutes: Int = -1, // UTC время в минутах
    val quantity: Float = 1f,
) {

    override fun toString(): String {
        return timeInMinutes.timeInMinutesToDisplay() // UTC отобразиться с учетом часового пояса
    }

    companion object {

        // максимальное количество приемов в день для одного препарата
        private const val MAX_COUNT_USAGES: Int = 30

        @JvmName("listUfToPattern")
        fun List<UsageFormat>.toPattern() = this.toSortedList()
            .joinToString(separator = ";", transform = { "${it.timeInMinutes}-${it.quantity}" })

        @JvmName("listUfToSort")
        private fun List<UsageFormat>.toSortedList(): List<UsageFormat> {
            val date = currentDateTimeInSecond() // UTC
            return this.toSortedSet(
                comparator = { up1, up2 ->
                    (date.changeTimeToSystemTimeInMinutes(up1.timeInMinutes) -
                            date.changeTimeToSystemTimeInMinutes(up2.timeInMinutes))
                }
            ).toList()
        }

        fun changeTimeUsagePattern(
            usagesPattern: List<UsageFormat>,
            pattern: UsageFormat,
            time: Int,
        ): List<UsageFormat> {
            return usagesPattern.minus(pattern)
                .plus(pattern.copy(timeInMinutes = time))
                .toSortedList()
        }

        fun changeQuantityUsagePattern(
            usagesPattern: List<UsageFormat>,
            pattern: UsageFormat,
            quantity: Float,
        ): List<UsageFormat> {
            return usagesPattern.minus(pattern)
                .plus(pattern.copy(quantity = quantity))
                .toSortedList()
        }

        fun addUsagesPattern(
            usagesPattern: List<UsageFormat>,
            dose: Float = 1f,
        ): List<UsageFormat> {
            // проверить на максимум
            if (usagesPattern.size >= MAX_COUNT_USAGES) return usagesPattern
            val date = currentDateTimeInSecond() // UTC
            var time = currentTimeInMinutes() // в UTC
            var isChange = false
            do {
                isChange = false
                usagesPattern.find {
                    date.changeTime(it.timeInMinutes) == date.changeTime(time)
                }?.let {
                    time++
                    isChange = true
                }
                if (time >= MINUTES_IN_DAY) {
                    time = 0
                    isChange = true
                }
            } while (isChange)
            return usagesPattern.plus(
                UsageFormat(
                    timeInMinutes = time,
                    quantity = dose,
                )
            )
                .toSortedList()
        }
    }
}
