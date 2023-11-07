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
private const val DAYS_IN_MONTH = 30 // округленно, дней в месяце
private const val TIMEZONE_CORRECT = 720 // 12 * 60
const val TIME_IS_UP = 3600
const val WEEKS_PER_MONTH = 6 // строк недель месяца
const val DAYS_A_WEEK = 7 // дней недели
const val LIMIT_START_MIN = 12 // - минимальная дата начала курса от текущей даты
const val LIMIT_START_MAX = 6 // + максимальная дата начала курса от текущей даты
const val LIMIT_END_MAX = 60 // + максимальная дата окончания курса от текущей даты

val notNullDateTime = 0L.toDateTimeUTC()

// Получение даты + времени
fun currentDateTimeInSeconds() = Clock.System.now().epochSeconds
fun currentDateTimeInMilliseconds() = Clock.System.now().toEpochMilliseconds()
fun currentDateTimeSystem() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

// преобразование
fun LocalDateTime.systemToInstant() = this.toInstant(TimeZone.currentSystemDefault())
private fun LocalDateTime.toInstant() = this.toInstant(TimeZone.UTC)
private fun Long.toInstant() = Instant.fromEpochSeconds(this)
fun String.toDateTimeUTCInSecond() = if (this == "") currentDateTimeInSeconds()
else LocalDateTime.parse(this).toEpochSecond()

fun Long.toDateTimeUTC() = this.toInstant().toLocalDateTime(TimeZone.UTC)
fun Long.toDateTimeSystem() = this.toInstant().toLocalDateTime(TimeZone.currentSystemDefault())

fun Instant.toDateTimeSystem() = this.toLocalDateTime(TimeZone.currentSystemDefault())
fun LocalDateTime.toEpochSecond() = this.toInstant(TimeZone.UTC).epochSeconds
fun LocalDateTime.systemToEpochSecond() = this.toInstant(TimeZone.currentSystemDefault())
    .epochSeconds

fun LocalDateTime.toUTC() = this.toInstant(TimeZone.currentSystemDefault())
    .toLocalDateTime(TimeZone.UTC)

fun LocalDateTime.toSystem() = this.toInstant(TimeZone.UTC)
    .toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.minusForNotification() = this.minus(DateTimePeriod(minutes = 5))

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

fun Long.displayDateTime() = this.toDateTimeSystem().displayDateTime()

fun Long.milliSecondsToDisplayDateTime() = if (this < MILES_SECONDS) ""
else (this / MILES_SECONDS).displayDateTime()

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
        .ofPattern("dd MMMM yyyy", Locale.getDefault())
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
fun Long.displayTime() = this.toDateTimeSystem().displayTime()

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

fun LocalDateTime.atNoonOfDay() = this.changeTime(
    hour = 12,
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

// поправка, чтобы число не было отрицательным
// 0..-720..+720..+1440 для часового пояса -12..+12, в сутках 24 часа * 60 = 1440
// 0 - -12, 720 - 0, 1440 - +12, 2160 - 24 часа
fun Long.timeCorrectToSystem(minute: Int) = this.toDateTimeSystem()
    .correctTimeInMinutes(minute.timeCorrectToSystem())

fun LocalDateTime.correctTimeInMinutes(minute: Int) = this
    .atStartOfDay()//  установить в локальном поясе 00 + минуты
    .systemToInstant()
    // тут время уже откоректировано и может быть отрицательным
    .plus(minute, DateTimeUnit.MINUTE, TimeZone.UTC) // тут UTC
    .toDateTimeSystem()

fun LocalDateTime.timeCorrect() = this.atStartOfDay().let {
    it.toInstant().minus(it.systemToInstant()).inWholeMinutes.toInt()
}

fun Int.timeCorrect() = this - currentDateTimeSystem().timeCorrect() + TIMEZONE_CORRECT
fun Int.timeCorrectToSystem() = this + currentDateTimeSystem().timeCorrect() - TIMEZONE_CORRECT

// текущее время преобразованное в минуты
fun Int.displayTimeInMinutes() = "%02d:%02d".format(this.hour(), this.minute())
fun Int.displayWithDateTimeInMinutes() = currentDateTimeSystem()
    .correctTimeInMinutes(this)
//    .timeInMinutes()
//    .displayTimeInMinutes()
//    .displayDateTimeShort()
    .displayTime()

fun Int.hourPlusMinute(minute: Int) = this * MINUTES_IN_HOUR + minute

fun Int.hour() = this / MINUTES_IN_HOUR

fun Int.minute() = this % MINUTES_IN_HOUR

fun LocalDateTime.timeInMinutes() = time.hour * MINUTES_IN_HOUR + time.minute

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

private fun Instant.betweenDays(other: Instant) = this.minus(other).inWholeDays

//для расчета разницы между датами + текущие сутки
fun LocalDateTime.betweenDays(date: LocalDateTime) = this
    .systemToInstant()
    .betweenDays(date.systemToInstant())

// тут время должно быть одинаковое
fun LocalDateTime.courseDuration(date: LocalDateTime) = this.atStartOfDay()
    .systemToInstant()
    .betweenDays(date.atStartOfDay().systemToInstant())

fun LocalDateTime.isEqualsDay(date: LocalDateTime) = this.dayOfYear == date.dayOfYear &&
    this.year == date.year

fun LocalDateTime.isEqualsDay(dateInMilliseconds: Long) =
    if (dateInMilliseconds < MILES_SECONDS) false
    else (dateInMilliseconds / MILES_SECONDS).toDateTimeSystem().isEqualsDay(this)

fun Long.isEqualsDay(dateInMilliseconds: Long) = if (dateInMilliseconds < MILES_SECONDS) false
else (dateInMilliseconds / MILES_SECONDS).toDateTimeSystem().isEqualsDay(this)

fun LocalDateTime.isBetweenDay(firstDate: LocalDateTime, lastDate: LocalDateTime): Boolean {
    val first = firstDate.atStartOfDay().systemToInstant()
    val last = lastDate.atEndOfDay().systemToInstant()
    return this.systemToInstant() in first..last
}

fun LocalDateTime.isWithinDay1(date: LocalDateTime): Boolean {
    val first = date.systemToInstant()
    val last = date.atEndOfDay().systemToInstant()
    return this.systemToInstant() in first..last
}

// интервал и дата оповещения
// endDate в локальном времени
fun Int.months() = this / DAYS_IN_MONTH
fun Int.days() = this % DAYS_IN_MONTH

fun Int.monthPlusDay(days: Int) = this * DAYS_IN_MONTH + days

fun LocalDateTime.nextCourseStart(
    coursesInterval: Int,
    remindBeforePeriod: Int,
    remindTime: Int
): Pair<LocalDateTime?, Long> {
    return try {
        val nextCourseStart = this
            .plusDays(coursesInterval)
            .atStartOfDay()
        val remindDate = if (coursesInterval > 0 || remindBeforePeriod > 0 || remindTime > 0) {
            nextCourseStart
                .minusDays(remindBeforePeriod)
                .plusDays(1) // т.е. напоминаем
                .changeTime(minutes = remindTime)
        } else null
        val interval = remindDate?.let {
            nextCourseStart.betweenDays(this.atStartOfDay()) // тут чтобы не было 0, на следующий день значит +1
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
    if (remindDate == null) return Triple(0, 0, 0)

    return try {
        val intervalCorrect =
            interval.minus(1) // -1 день, тут чтобы не было 0, на следующий день значит +1
                .takeIf { it in 0..390 } // не больше 12 месяцев и 30 дней
                ?: 0

        val remindTime = remindDate.timeInMinutes()
        val nextCourseStart = this.plusDays(intervalCorrect).atStartOfDay()
        val beforeDay = nextCourseStart.betweenDays(remindDate.atStartOfDay())
            .takeIf { it in 0..390 } // не больше 12 месяцев и 30 дней
            ?: 0

        Triple(remindTime, intervalCorrect.toInt(), beforeDay.toInt())
    } catch (_: Error) {
        Triple(0, 0, 0)
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

//
fun LocalDateTime.minOf(b: LocalDateTime?, c: LocalDateTime? = null): LocalDateTime {
    return when {
        b != null && c == null -> if (this <= b) this else b
        b == null && c != null -> if (this <= c) this else c
        b != null && c != null -> this.minOf(b.minOf(c))
        else -> this
    }
}
