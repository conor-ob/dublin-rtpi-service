package io.rtpi.service.luas

import io.rtpi.api.LiveTime
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.time.toIso8601
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val dublin = ZoneId.of("Europe/Dublin")

class LuasLiveDataService(rtpiApi: RtpiApi) : AbstractLuasLiveDataService(rtpiApi) {

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
