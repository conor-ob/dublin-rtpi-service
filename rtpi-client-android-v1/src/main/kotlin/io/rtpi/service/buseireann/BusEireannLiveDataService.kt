package io.rtpi.service.buseireann

import io.rtpi.api.LiveTime
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.time.toIso8601
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

private val dublin = ZoneId.of("Europe/Dublin")

class BusEireannLiveDataService(rtpiApi: RtpiApi) : AbstractBusEireannLiveDataService(rtpiApi) {

    override fun createDueTime(serverTimestamp: String, json: RtpiRealTimeBusInformationJson): LiveTime {
        val serverDateTime = parseDateTime(serverTimestamp)

        val scheduledArrivalDateTime = parseDateTime(json.scheduledArrivalDateTime!!)
        val expectedArrivalDateTime = parseDateTime(json.arrivalDateTime!!)

        return LiveTime(
            currentTimestamp = serverDateTime.toIso8601(),
            waitTimeMinutes = minutesBetween(serverDateTime, expectedArrivalDateTime),
            lateTimeMinutes = minutesBetween(scheduledArrivalDateTime, expectedArrivalDateTime),
            scheduledArrivalTimestamp = scheduledArrivalDateTime.toIso8601(),
            expectedArrivalTimestamp = expectedArrivalDateTime.toIso8601(),
            scheduledDepartureTimestamp = parseDateTime(json.scheduledDepartureDateTime!!).toIso8601(),
            expectedDepartureTimestamp = parseDateTime(json.departureDateTime!!).toIso8601()
        )
    }

    private fun parseDateTime(timestamp: String): ZonedDateTime {
        return LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).atZone(dublin)
    }

    private fun minutesBetween(start: ZonedDateTime, end: ZonedDateTime): Int {
        val minutes = Duration.between(start, end).toMinutes().toInt()
        return if (minutes > -1) {
            minutes
        } else {
            0
        }
    }

}
