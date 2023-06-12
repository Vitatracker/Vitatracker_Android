package app.mybad.notifier.utils

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private const val SECONDS_IN_DAY = 86400L

// форматирование даты и времени
private val dateDisplayFormatter = DateTimeFormatter
    .ofPattern("dd.MM.yyyy")
    .withZone(ZoneOffset.UTC)

fun Long.plusDay() = this + SECONDS_IN_DAY

fun Long.plusThreeDay() = this + SECONDS_IN_DAY * 3 + 1

fun Long.secondsToDay() = (this % SECONDS_IN_DAY).toInt()

fun Long.toDateDisplay() = Instant.ofEpochSecond(this).toDateFormat()

fun Instant.toDateFormat() = dateDisplayFormatter.format(this)
