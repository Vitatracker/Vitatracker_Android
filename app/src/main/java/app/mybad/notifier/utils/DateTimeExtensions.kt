package app.mybad.notifier.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private const val SECONDS_IN_DAY = 86400L

// форматирование даты и времени
private val dateDisplayFormatter = DateTimeFormatter
    .ofPattern("dd.MM.yyyy")
    .withZone(ZoneOffset.UTC)

fun Long.plusDay(): Long = this + SECONDS_IN_DAY

fun Long.plusThreeDay(): Long = this + SECONDS_IN_DAY * 3 + 1

fun Long.secondsToDay(): Int = (this % SECONDS_IN_DAY).toInt()

fun Long.toDateDisplay(): String = Instant.ofEpochSecond(this).toDateFormat()

fun Instant.toDateFormat(): String = dateDisplayFormatter.format(this)

fun LocalDateTime.toLong(): Long {
    val zdt: ZonedDateTime = ZonedDateTime.of(this, ZoneId.systemDefault())
    return zdt.toInstant().toEpochMilli() / 1000
}
