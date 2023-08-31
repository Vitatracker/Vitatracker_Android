package app.mybad.data.models

import app.mybad.utils.SECONDS_IN_HOUR
import app.mybad.utils.currentTimeInMinutes

data class UsageFormat(
    val timeInMinutes: Int = -1,
    val quantity: Int = 0,
) {

    companion object {

        // максимальное количество приемов в день для одного препарата
        private const val MAX_COUNT_USAGES: Int = 30

        private fun List<UsageFormat>.toSort() = this.toSortedSet(
            comparator = { up1, up2 ->
                up1.timeInMinutes - up2.timeInMinutes
            }
        ).toList()

        fun changeTimeUsagePattern(
            usagesPattern: List<UsageFormat>,
            pattern: UsageFormat,
            time: Int
        ): List<UsageFormat> {
            return usagesPattern.minus(pattern)
                .plus(pattern.copy(timeInMinutes = time))
                .toSort()
        }

        fun changeQuantityUsagePattern(
            usagesPattern: List<UsageFormat>,
            pattern: UsageFormat,
            quantity: Int
        ): List<UsageFormat> {
            return usagesPattern.minus(pattern)
                .plus(pattern.copy(quantity = quantity))
                .toSort()
        }

        fun addUsagesPattern(usagesPattern: List<UsageFormat>): List<UsageFormat> {
            // проверить на максимум
            if (usagesPattern.size >= MAX_COUNT_USAGES) return usagesPattern
            var time = currentTimeInMinutes()
            var isChange = false
            do {
                isChange = false
                usagesPattern.find { it.timeInMinutes == time }?.let {
                    time++
                    isChange = true
                }
                if (time >= SECONDS_IN_HOUR) {
                    time = 0
                    isChange = true
                }
            } while (isChange)
            return usagesPattern.plus(
                UsageFormat(
                    timeInMinutes = time,
                    quantity = 1
                )
            )
                .toSort()
        }
    }
}