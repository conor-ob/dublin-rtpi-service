package io.rtpi.time

import org.threeten.bp.Clock
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

private const val dublinZoneId = "Europe/Dublin"

object DateTimeProvider {

    private var clock: Clock = Clock.system(ZoneId.of(dublinZoneId))

    fun getCurrentDate(): LocalDate = getCurrentDateTime().toLocalDate()

    fun getCurrentDateTime(): ZonedDateTime = ZonedDateTime.now(clock)

    fun getDateTime(timestamp: String, formatter: DateTimeFormatter): ZonedDateTime =
        LocalDateTime.parse(timestamp, formatter).atZone(clock.zone)

//    fun getTime(timestamp: String, formatter: DateTimeFormatter): LocalTime {
//        LocalTime.parse(timestamp, formatter).atDate(get)
//    }

    fun setClock(clock: Clock) {
        DateTimeProvider.clock = clock
    }
}

fun ZonedDateTime.toIso8601(): String = toOffsetDateTime().toString()