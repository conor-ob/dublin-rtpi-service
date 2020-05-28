package io.rtpi.time

import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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
