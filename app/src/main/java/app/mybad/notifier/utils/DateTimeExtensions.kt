package app.mybad.notifier.utils

import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val SECONDS_IN_DAY = 86400L

// форматирование даты и времени
private val dateDisplayFormatter = DateTimeFormatter
    .ofPattern("dd.MM.yyyy")
    .withZone(ZoneOffset.systemDefault())

fun Long.toDateDisplay(): String = Instant.fromEpochSeconds(this).formatDate()

fun Instant.formatDate(): String = dateDisplayFormatter.format(this.toJavaInstant())

private val dateFullDisplayFormatter
    get() = DateTimeFormatter
        .ofPattern(
            "dd MMMM yyyy",
            Locale.getDefault()
        )
        .withZone(ZoneOffset.systemDefault())

fun LocalDateTime.toDateFullDisplay(): String =
    this.toInstant(TimeZone.currentSystemDefault()).formatDateFull()

fun Instant.formatDateFull(): String = dateFullDisplayFormatter.format(this.toJavaInstant())

private val timeDisplayFormatter = DateTimeFormatter
    .ofPattern("HH:mm")
    .withZone(ZoneOffset.systemDefault())

fun Long.toTimeDisplay(): String = Instant.fromEpochSeconds(this).formatTime()

fun LocalDateTime.toTimeDisplay(): String = this.toInstant(TimeZone.UTC).formatTime()

fun Instant.formatTime(): String = timeDisplayFormatter.format(this.toJavaInstant())

private val dayDisplayFormatter
    get() = DateTimeFormatter
        .ofPattern(
            "dd MMMM",
            Locale.getDefault()
        )
        .withZone(ZoneOffset.systemDefault())

fun Long.toDayDisplay(): String = Instant.fromEpochSeconds(this).formatDay()

fun LocalDateTime.toDayDisplay(): String = this.toInstant(TimeZone.UTC).formatDay()

fun Instant.formatDay(): String = dayDisplayFormatter.format(this.toJavaInstant())

fun Long.plusDay(): Long = this + SECONDS_IN_DAY

fun Long.plusThreeDay(): Long = this + SECONDS_IN_DAY * 3 + 1

fun Long.secondsToDay() = (this % SECONDS_IN_DAY)

fun Long.daysBetween(date: Long) = Instant.fromEpochSeconds(this)
    .minus(Instant.fromEpochSeconds(date)).inWholeDays.plus(1)

fun Long.toLocalDateTime() = Instant.fromEpochSeconds(this)
    .toLocalDateTime(TimeZone.UTC)

fun Long.toSystemDateTime() = Instant.fromEpochSeconds(this)
    .toLocalDateTime(TimeZone.currentSystemDefault())

fun Long.toSecondsLeftFromStartOfDay() = this - this.secondsToDay()

fun LocalDateTime.toSecondsLeftFromStartOfDay() =
    this.toEpochSecond().toSecondsLeftFromStartOfDay()

fun LocalDateTime.toEpochSecond(isUTC: Boolean = true) = this.toInstant(
    if (isUTC) TimeZone.UTC else TimeZone.currentSystemDefault()
).epochSeconds

// Преобразование и замена даты и времени
fun LocalDateTime.changeTime(hour: Int, minute: Int) = LocalDateTime(
    year = this.year,
    monthNumber = this.monthNumber,
    dayOfMonth = this.dayOfMonth,
    hour = hour,
    minute = minute,
    second = 0,
    nanosecond = 0,
)

fun LocalDateTime.changeTime(time: LocalDateTime) = LocalDateTime(
    year = this.year,
    monthNumber = this.monthNumber,
    dayOfMonth = this.dayOfMonth,
    hour = time.hour,
    minute = time.minute,
    second = time.second,
    nanosecond = time.nanosecond,
)

fun LocalDateTime.changeTime(time: Long) = time.toLocalDateTime().let {
    LocalDateTime(
        year = this.year,
        monthNumber = this.monthNumber,
        dayOfMonth = this.dayOfMonth,
        hour = it.hour,
        minute = it.minute,
        second = it.second,
        nanosecond = it.nanosecond,
    )
}

fun LocalDateTime.changeDayOfMonth(dayOfMonth: Int) = LocalDateTime(
    year = this.year,
    monthNumber = this.monthNumber,
    dayOfMonth = dayOfMonth,
    hour = this.hour,
    minute = this.minute,
    second = this.second,
    nanosecond = this.nanosecond,
)

fun LocalDateTime.changeMonth(month: Int) = LocalDateTime(
    year = this.year,
    monthNumber = month,
    dayOfMonth = this.dayOfMonth,
    hour = this.hour,
    minute = this.minute,
    second = this.second,
    nanosecond = this.nanosecond,
)

fun LocalDateTime.changeDate(year: Int? = null, month: Int? = null, dayOfMonth: Int? = null) =
    LocalDateTime(
        year = year ?: this.year,
        monthNumber = month ?: this.monthNumber,
        dayOfMonth = dayOfMonth ?: this.dayOfMonth,
        hour = this.hour,
        minute = this.minute,
        second = this.second,
        nanosecond = this.nanosecond,
    )

fun LocalDateTime.atStartOfDay() = LocalDateTime(
    year = this.year,
    monthNumber = this.monthNumber,
    dayOfMonth = this.dayOfMonth,
    hour = 0,
    minute = 0,
    second = 0,
    nanosecond = 0,
)

fun LocalDateTime.atStartOfDaySystemToUTC() = this.atStartOfDay()
    .toInstant(TimeZone.currentSystemDefault())
    .toLocalDateTime(TimeZone.UTC)

fun Long.atStartOfDay() = Instant.fromEpochSeconds(this)
    .toLocalDateTime(TimeZone.UTC)
    .atStartOfDay()
    .toEpochSecond()

fun Long.atStartOfDaySystemToUTC() = Instant.fromEpochSeconds(this)
    .toLocalDateTime(TimeZone.UTC)
    .atStartOfDaySystemToUTC()
//    .toEpochSeconds()

fun LocalDateTime.atEndOfDay() = LocalDateTime(
    year = this.year,
    monthNumber = this.monthNumber,
    dayOfMonth = this.dayOfMonth,
    hour = 23,
    minute = 59,
    second = 59,
    nanosecond = 999,
)

fun LocalDateTime.atEndOfDaySystemToUTC() = this.atEndOfDay()
    .toInstant(TimeZone.currentSystemDefault())
    .toLocalDateTime(TimeZone.UTC)

fun Long.atEndOfDay() = Instant.fromEpochSeconds(this)
    .toLocalDateTime(TimeZone.UTC)
    .atEndOfDay()
    .toEpochSecond()

fun Long.atEndOfDaySystemToUTC() = Instant.fromEpochSeconds(this)
    .toLocalDateTime(TimeZone.UTC)
    .atEndOfDaySystemToUTC()
//    .toEpochSeconds()

fun LocalDateTime.atStartOfMonth() = this.changeDayOfMonth(1)

// Прибавление и удаление
fun LocalDateTime.minus(period: DateTimePeriod) = this.toInstant(TimeZone.UTC)
    .minus(period, TimeZone.UTC)
    .toLocalDateTime(TimeZone.UTC)

fun LocalDateTime.minusDays(days: Int): LocalDateTime {
    return if (days == 0) this
    else this.toInstant(TimeZone.UTC)
        .minus(days, DateTimeUnit.DAY, TimeZone.UTC)
        .toLocalDateTime(TimeZone.UTC)
}

fun LocalDateTime.minusMonths(months: Int): LocalDateTime {
    return if (months == 0) this
    else this.toInstant(TimeZone.UTC)
        .minus(months, DateTimeUnit.MONTH, TimeZone.UTC)
        .toLocalDateTime(TimeZone.UTC)
}

fun LocalDateTime.plus(period: DateTimePeriod) = this.toInstant(TimeZone.UTC)
    .plus(period, TimeZone.UTC)
    .toLocalDateTime(TimeZone.UTC)

fun LocalDateTime.plusDays(days: Int): LocalDateTime {
    return if (days == 0) this
    else this.toInstant(TimeZone.UTC)
        .plus(days, DateTimeUnit.DAY, TimeZone.UTC)
        .toLocalDateTime(TimeZone.UTC)
}

fun LocalDateTime.plusMonths(months: Int): LocalDateTime {
    return if (months == 0) this
    else this.toInstant(TimeZone.UTC)
        .plus(months, DateTimeUnit.MONTH, TimeZone.UTC)
        .toLocalDateTime(TimeZone.UTC)
}

// Получение даты + времени
fun getCurrentDateTime() = now().toLocalDateTime(TimeZone.UTC)

val Int.isLeapYear
    get() = when {
        this % 4 == 0 -> {
            when {
                this % 100 == 0 -> this % 400 == 0
                else -> true
            }
        }

        else -> false
    }
