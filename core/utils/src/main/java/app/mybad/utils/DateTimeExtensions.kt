package app.mybad.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

const val DELAY_INTENT_SECONDS = 30
const val MINUTES_IN_DAY = 1440
private const val MINUTES_IN_HOUR = 60
const val MILES_SECONDS = 1000
const val TIME_IS_UP = 3600
const val TIME_NOTIFICATION = 840 // время в минутах 14:00
const val WEEKS_PER_MONTH = 6 // строк недель месяца
const val DAYS_A_WEEK = 7 // дней недели
const val LIMIT_START_MIN = 12 // - минимальная дата начала курса от текущей даты
const val LIMIT_START_MAX = 6 // + максимальная дата начала курса от текущей даты
const val LIMIT_END_MAX = 60 // + максимальная дата окончания курса от текущей даты

val notNullDateTime = 0L.toDateTimeUTC()

// Получение даты + времени
fun currentDateTimeUTCInSecond() = currentDateTimeUTC().toEpochSecond()
private fun currentDateTimeUTC() = Clock.System.now().toLocalDateTime(TimeZone.UTC)
fun currentDateTimeSystem() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

// преобразование
fun LocalDateTime.systemToInstant() = this.toInstant(TimeZone.currentSystemDefault())
private fun Long.toInstant() = Instant.fromEpochSeconds(this)
fun String.toDateTimeUTCInSecond() = if (this == "") currentDateTimeUTCInSecond()
else LocalDateTime.parse(this).toEpochSecond()

fun Long.toDateTimeUTC() = this.toInstant().toLocalDateTime(TimeZone.UTC)
fun Long.toDateTimeSystem() = this.toInstant().toLocalDateTime(TimeZone.currentSystemDefault())
fun Long.toDateTimeSystem(minutesUTC: Int) = this.toInstant()
    .toLocalDateTime(TimeZone.UTC).changeTime(minutesUTC)
    .toInstant(TimeZone.UTC)
    .toLocalDateTime(TimeZone.currentSystemDefault())
fun Instant.toDateTimeSystem() = this.toLocalDateTime(TimeZone.currentSystemDefault())
fun LocalDateTime.toEpochSecond() = this.toInstant(TimeZone.UTC).epochSeconds
fun LocalDateTime.systemToEpochSecond() = this.toInstant(TimeZone.currentSystemDefault())
    .epochSeconds

fun LocalDateTime.toUTC() = this.toInstant(TimeZone.currentSystemDefault())
    .toLocalDateTime(TimeZone.UTC)

fun LocalDateTime.toSystem() = this.toInstant(TimeZone.UTC)
    .toLocalDateTime(TimeZone.currentSystemDefault())

// форматирование даты и времени
private val dateTimeDisplayFormatter = DateTimeFormatter
    .ofPattern("dd.MM.yyyy HH:mm:ss")
    .withZone(ZoneOffset.systemDefault())

fun Long.displayDateTimeUTC() = this.toInstant()
    .formatDateTime()

private fun Instant.formatDateTime(): String = dateTimeDisplayFormatter.format(this.toJavaInstant())

private const val dateTimeFormatter = "%02d.%02d.%04d %02d:%02d:%02d" // dd.MM.yyyy HH:mm:ss
fun LocalDateTime.displayDateTime() = dateTimeFormatter.format(
    dayOfMonth,
    month.value,
    year,
    hour,
    minute,
    second,
)

private const val dateFormatter = "%02d.%02d.%04d" // dd.MM.yyyy
fun LocalDateTime.displayDate() = dateFormatter.format(
    dayOfMonth,
    month.value,
    year,
)

private const val dateTimeShortFormatter = "%02d.%02d %02d:%02d" // dd.MM HH:mm
fun LocalDateTime.displayDateTimeShort() = dateTimeShortFormatter.format(
    dayOfMonth,
    month.value,
    hour,
    minute,
)

private val dateFullDisplayFormatter
    get() = DateTimeFormatter
        .ofPattern(
            "dd MMMM yyyy",
            Locale.getDefault()
        )
        .withZone(ZoneOffset.systemDefault())

fun LocalDateTime.displayDateFull(): String = this.systemToInstant()
    .formatDateFull()

private fun Instant.formatDateFull(): String = dateFullDisplayFormatter.format(this.toJavaInstant())

private val dayAndMonthFullFormatter
    get() = DateTimeFormatter
        .ofPattern(
            "dd MMMM",
            Locale.getDefault()
        )
        .withZone(ZoneOffset.systemDefault())


fun LocalDateTime.displayDayAndMonthFull(): String = this.systemToInstant()
    .formatDayAndMonthFull()

private fun Instant.formatDayAndMonthFull(): String =
    dayAndMonthFullFormatter.format(this.toJavaInstant())

private const val dayOfMonthFormatter = "%02d"
fun LocalDateTime.displayDay() = dayOfMonthFormatter.format(this.dayOfMonth)

private const val timeFormatter = "%02d:%02d" //HH:mm
fun LocalDateTime.displayTime() = timeFormatter.format(hour, minute)

//DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
private const val dateTimeIsoFormatter = "%04d-%02d-%02dT%02d:%02d:%02dZ"

fun Long.toDateTimeIso(): String {
    return if (this == 0L) ""
    else this.toDateTimeUTC().formatISO()
}

private fun LocalDateTime.formatISO() = dateTimeIsoFormatter.format(
    year,
    month.value,
    dayOfMonth,
    hour,
    minute,
    second,
)

// названия месяцев и дней
fun Int.monthShortDisplay(): String = Month(this + 1).getDisplayName(
    TextStyle.SHORT_STANDALONE,
    Locale.getDefault()
).uppercase(Locale.getDefault()).replace(".", "")

fun Int.monthFullDisplay(): String = Month(this + 1).getDisplayName(
    TextStyle.FULL_STANDALONE,
    Locale.getDefault()
).replaceFirstChar { it.uppercase(Locale.getDefault()) }

fun DayOfWeek.displayName() = this.getDisplayName(
    TextStyle.SHORT_STANDALONE,
    Locale.getDefault()
).uppercase(Locale.getDefault())

fun Int.dayShortDisplay() = DayOfWeek(this + 1).displayName()

fun LocalDateTime.dayShortDisplay() = this.dayOfWeek.displayName()

fun Int.dayFullDisplay(): String = DayOfWeek(this + 1).getDisplayName(
    TextStyle.FULL_STANDALONE,
    Locale.getDefault()
).replaceFirstChar { it.uppercase(Locale.getDefault()) }

// Преобразование и замена даты и времени
// тут дата не меняется, а время меняется из UTC и подменяется в локальной дате
fun LocalDateTime.changeTimeOfUTC_(minutesUTC: Int) = this
    .toUTC()
    .changeTime(minutes = minutesUTC)
    .toSystem()
    .let { this.changeTime(hour = it.hour, minute = it.minute) }

fun LocalDateTime.changeTime(minutes: Int) = this.changeTime(
    hour = minutes / MINUTES_IN_HOUR,
    minute = minutes % MINUTES_IN_HOUR,
)

fun LocalDateTime.changeTime(hour: Int, minute: Int) = LocalDateTime(
    year = this.year,
    monthNumber = this.monthNumber,
    dayOfMonth = this.dayOfMonth,
    hour = hour,
    minute = minute,
    second = 0,
    nanosecond = 0,
)

private fun LocalDateTime.changeTime(
    hour: Int = 0,
    minute: Int = 0,
    second: Int = 0,
    nanosecond: Int = 0
) = LocalDateTime(
    year = this.year,
    monthNumber = this.monthNumber,
    dayOfMonth = this.dayOfMonth,
    hour = hour,
    minute = minute,
    second = second,
    nanosecond = nanosecond,
)

// month: 1..12, days: 1..31
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

fun LocalDateTime.atStartOfDay() = this.changeTime(
    hour = 0,
    minute = 0,
    second = 0,
    nanosecond = 0
)

fun LocalDateTime.atEndOfDay() = this.changeTime(
    hour = 23,
    minute = 59,
    second = 59,
    nanosecond = 0
)

// Прибавление и удаление
// работаем только с локальными датами с учетом часового пояса
private fun Instant.minus(period: DateTimePeriod) = this
    .minus(period, TimeZone.currentSystemDefault())

fun LocalDateTime.minus(period: DateTimePeriod) = this
    .systemToInstant()
    .minus(period)
    .toDateTimeSystem()

private fun Instant.minusDays(days: Int = 1) = this
    .minus(days, DateTimeUnit.DAY, TimeZone.currentSystemDefault())

fun LocalDateTime.minusDays(days: Int = 1) = if (days == 0) this
else this.systemToInstant()
    .minusDays(days)
    .toDateTimeSystem()

private fun Instant.minusMonths(months: Int = 1) = this
    .minus(months, DateTimeUnit.MONTH, TimeZone.currentSystemDefault())

fun LocalDateTime.minusMonths(months: Int) = if (months == 0) this
else this.systemToInstant()
    .minusMonths(months)
    .toDateTimeSystem()

// Прибавление
private fun Instant.plus(period: DateTimePeriod) = this
    .plus(period, TimeZone.currentSystemDefault())

fun LocalDateTime.plus(period: DateTimePeriod) = this
    .systemToInstant()
    .plus(period)
    .toDateTimeSystem()

private fun Instant.plusSeconds(seconds: Int = 1) = this
    .plus(seconds, DateTimeUnit.SECOND, TimeZone.currentSystemDefault())

fun LocalDateTime.plusSeconds(seconds: Int = 1) = if (seconds == 0) this
else this.systemToInstant()
    .plusSeconds(seconds)
    .toDateTimeSystem()

private fun Instant.plusDays(days: Int = 1) = this
    .plus(days, DateTimeUnit.DAY, TimeZone.currentSystemDefault())

fun LocalDateTime.plusDays(days: Long) = this.plusDays(days.toInt())
fun LocalDateTime.plusDays(days: Int = 1) = if (days == 0) this
else this.systemToInstant()
    .plusDays(days)
    .toDateTimeSystem()

private fun Instant.plusMonths(months: Int = 1) = this
    .plus(months, DateTimeUnit.MONTH, TimeZone.currentSystemDefault())

fun LocalDateTime.plusMonths(months: Int) = if (months == 0) this
else this.systemToInstant()
    .plusMonths(months)
    .toDateTimeSystem()

// текущее время преобразованное в минуты
fun Int.displayTimeInMinutes() = "%02d:%02d".format(this.hour(), this.minute())

fun Int.hourPlusMinute(minute: Int) = this * MINUTES_IN_HOUR + minute

fun Int.hour() = this / MINUTES_IN_HOUR

fun Int.minute() = this % MINUTES_IN_HOUR

fun LocalDateTime.timeInMinutes() = time.hour * MINUTES_IN_HOUR + time.minute

// изменить время у текущей даты и преобразовать в UTC и отдать только время
private fun timeInMinutesSystemToUTC(hour: Int, minute: Int) = currentDateTimeSystem()
    .changeTime(hour = hour, minute = minute)
    .toUTC()
    .timeInMinutes()

fun Int.timeInMinutesSystemToUTC() = timeInMinutesSystemToUTC(hour = hour(), minute = minute())

// изменить время у текущей даты и преобразовать с учетом часового пояса и отдать только время
fun Int.timeInMinutesUTCToSystem() = currentDateTimeUTC()
    .changeTime(minute = minute(), hour = hour())
    .toSystem()
    .timeInMinutes()

// высокосный год
private val Int.isLeapYear
    get() = when {
        this % 4 == 0 -> {
            when {
                this % 100 == 0 -> this % 400 == 0
                else -> true
            }
        }

        else -> false
    }

fun LocalDateTime.getDaysOfMonth() = month.length(year.isLeapYear)

// первый и последний дни месяца с учетом высокосного года
fun LocalDateTime.firstDayOfMonth() = this.changeDate(dayOfMonth = 1)
    .changeTime(hour = 12, minute = 0) // выставим в 12:00

fun LocalDateTime.lastDayOfMonth() = this.changeDate(dayOfMonth = this.getDaysOfMonth())
    .atEndOfDay()

// сравнение месяцев и года, т.е. тот же месяц или нет
fun LocalDateTime.isEqualsMonth(date: LocalDateTime) = this.month == date.month &&
    this.year == date.year

fun LocalDateTime.isNotEqualsMonth(date: LocalDateTime) = !this.isEqualsMonth(date)

// разница между днями
private fun Instant.betweenSeconds(other: Instant) = this.minus(other).inWholeSeconds
fun LocalDateTime.betweenSecondsSystem(date: LocalDateTime) = this
    .systemToInstant()
    .betweenSeconds(date.systemToInstant())

private fun Instant.betweenDays(other: Instant) = this.minus(other).inWholeDays.plus(1)
fun LocalDateTime.betweenDaysSystem(date: LocalDateTime) = this
    .systemToInstant()
    .betweenDays(date.systemToInstant())

fun LocalDateTime.isEqualsDay(date: LocalDateTime) = this.dayOfYear == date.dayOfYear &&
    this.year == date.year

fun LocalDateTime.isBetweenDay(firstDate: LocalDateTime, lastDate: LocalDateTime): Boolean {
    val first = firstDate.atStartOfDay().systemToInstant()
    val last = lastDate.atEndOfDay().systemToInstant()
    return this.systemToInstant() in first..last
}

// интервал и дата оповещения
// endDate в локальном времени
fun LocalDateTime.nextCourseStart(
    coursesInterval: DateTimePeriod,
    remindBeforePeriod: DateTimePeriod,
    remindTime: Int
): Pair<LocalDateTime?, Long> {
    return try {
        val nextCourseStart = this
            .plus(coursesInterval)
            .atStartOfDay()
        val remindDate = if (coursesInterval.months > 0 || coursesInterval.days > 0
            || remindBeforePeriod.months > 0 || remindBeforePeriod.days > 0
        ) {
            nextCourseStart
                .minus(remindBeforePeriod)
                .changeTime(minute = remindTime, hour = 0)
        } else null
        val interval = remindDate?.let {
            nextCourseStart.betweenDaysSystem(this.atStartOfDay())
        } ?: 0L
        remindDate to interval
    } catch (_: Error) {
        null to 0L
    }
}

//endDate в локальном времени
fun LocalDateTime.nextCourseIntervals(
    remindDate: LocalDateTime?,// remindDate в локальном времени
    interval: Long,
): Triple<Int, Int, Int> {
    // начало нового курса + интервал за сколько дней сообщить - последний день курса, который может быть больше
    // remindTime, coursesInterval (day), remindBeforePeriod (day)
    if (remindDate == null) return Triple(TIME_NOTIFICATION, 0, 0)

    return try {
        // не больше 12 месяцев и 30 дней
        val intervalCorrect = if (interval in 0..390) {
            interval.toInt() - 1 // -1 день, значит следующий день
        } else 0

        val remindTime = remindDate.timeInMinutes()
        val nextCourseStart = this.plusDays(intervalCorrect).atStartOfDay()
        val days = nextCourseStart.betweenDaysSystem(remindDate.atStartOfDay())
        // не больше 12 месяцев и 30 дней
        val beforeDay = if (days in 0..390) days else 0

        Triple(remindTime, intervalCorrect, beforeDay.toInt())
    } catch (_: Error) {
        Triple(TIME_NOTIFICATION, 0, 0)
    }
}

// начало месяца и от этого дня берется неделя и первый день недели матрица 6 х 7
fun LocalDateTime.repeatWeekAndDayOfMonth(
    action: (week: Int, day: Int, date: LocalDateTime) -> Unit = { _, _, _ -> }
) {
    val fwd = this.firstDayOfMonth()
    return repeat(WEEKS_PER_MONTH) { week ->
        repeat(DAYS_A_WEEK) { day ->
            val date = if (week == 0 && day < fwd.dayOfWeek.value) {
                fwd.minusDays(fwd.dayOfWeek.ordinal - day)
            } else {
                fwd.plusDays(week * DAYS_A_WEEK - fwd.dayOfWeek.ordinal + day)
            }
            action(week, day, date)
        }
    }
}

fun initWeekAndDayOfMonth(
    initData: LocalDateTime,
    action: (week: Int, day: Int, date: LocalDateTime) -> Unit = { _, _, _ -> }
): Array<Array<LocalDateTime>> {
    val fwd = initData.firstDayOfMonth()
    return Array(WEEKS_PER_MONTH) { week ->
        Array(DAYS_A_WEEK) { day ->
            val date = if (week == 0 && day < fwd.dayOfWeek.value) {
                fwd.minusDays(fwd.dayOfWeek.ordinal - day)
            } else {
                fwd.plusDays(week * DAYS_A_WEEK - fwd.dayOfWeek.ordinal + day)
            }
            action(week, day, date)
            date
        }
    }
}
