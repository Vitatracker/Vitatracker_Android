package app.mybad.domain.models

data class PatternUsageDomainModel(
    val id: Long = 0,
    val idn: Long = 0,

    val userId: Long = AuthToken.userId,
    val userIdn: String = "",

    val courseId: Long = -1,
    val courseIdn: Long = 0,

    val createdDate: Long = 0,
    val updatedDate: Long = 0,

    val timeInMinutes: Int = 0, // тут только время HH:mm в минутах
    val quantity: Float = 1f,

    val updateNetworkDate: Long = 0,
)

// кол-во приемов, минимальное и максимальное кол-во за 1 прием
fun String.patternToCount() = this.toPatterns().let { patterns ->
    if (patterns.isNotEmpty()) {
        Triple(
            patterns.size,
            patterns.minBy { it.second }.second,
            patterns.maxBy { it.second }.second
        )
    } else Triple(0, 0f, 0f)
}

// строка паттерн: [время приема в минутах от 00:00 до 23:59]-[количество препарата за этот прием];
fun String.toPatterns() = try {
    this.split(";").map {
        val pattern = it.split("-")
        if (pattern.size == 2) {
            (pattern[0].toIntOrNull() ?: 0) to (pattern[1].toFloatOrNull() ?: 0f)
        } else 0 to 0f
    }
} catch (_: Error) {
    emptyList()
}

