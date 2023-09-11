package app.mybad.data.models

import app.mybad.data.models.UsageFormat.Companion.toPatterns
import app.mybad.domain.models.CourseDisplayDomainModel
import app.mybad.utils.SECONDS_IN_HOUR
import app.mybad.utils.currentTimeInMinutes
import app.mybad.utils.toTimeDisplay

data class UsageFormat(
    val timeInMinutes: Int = -1, //
    val quantity: Float = 1f,
) {

    override fun toString(): String {
        return "${timeInMinutes.toTimeDisplay()}-$quantity"
    }

    companion object {

        // максимальное количество приемов в день для одного препарата
        private const val MAX_COUNT_USAGES: Int = 30

        // кол-во приемов, минимальное и максимальное кол-во за 1 прием
        fun CourseDisplayDomainModel.patternToCount() = this.toPatterns().let { patterns ->
            if (patterns.isNotEmpty()) {
                Triple(patterns.size, patterns.minBy { it.quantity }.quantity, patterns.maxBy { it.quantity }.quantity)
            } else Triple(0, 0f, 0f)
        }

        // строка паттерн: [время приема в минутах от 00:00 до 23:59]-[количество препарата за этот прием];
        fun CourseDisplayDomainModel.toPatterns(): List<UsageFormat> {
            return try {
                this.patternUsages.split(";").map {
                    val pattern = it.split("-")
                    if (pattern.size == 2) {
                        UsageFormat(
                            timeInMinutes = (pattern[0].toIntOrNull() ?: 0),
                            quantity = (pattern[1].toFloatOrNull() ?: 0f),
                        )
                    } else UsageFormat(0, 0f)
                }
            } catch (_: Error) {
                emptyList()
            }
        }

        @JvmName("listUfToPattern")
        fun List<UsageFormat>.toPattern() = this.toSort()
            .joinToString(separator = ";", transform = { it.toString() })

        @JvmName("listUfToSort")
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
            quantity: Float
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
                    quantity = 1f,
                )
            )
                .toSort()
        }
    }
}
