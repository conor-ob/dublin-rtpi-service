package io.rtpi.service.irishrail

import io.rtpi.api.LiveTime
import io.rtpi.resource.irishrail.IrishRailApi
import io.rtpi.resource.irishrail.IrishRailStationDataXml
import io.rtpi.time.toIso8601
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val dublin = ZoneId.of("Europe/Dublin")

class IrishRailLiveDataService(irishRailApi: IrishRailApi) : AbstractIrishRailLiveDataService(irishRailApi) {

    override fun createDueTime(xml: IrishRailStationDataXml): LiveTime {
        val serverDateTime = LocalDateTime.parse(xml.serverTime, DateTimeFormatter.ISO_DATE_TIME).atZone(dublin)
        val serverDate = serverDateTime.toLocalDate()

        val midnight = LocalTime.of(0, 0).atDate(serverDate).atZone(dublin)
        val scheduledArrivalDateTime = parseTime(xml.schArrival!!, serverDate)
        val expectedArrivalDateTime = parseTime(xml.expArrival!!, serverDate)
        val scheduledDepartureDateTime = parseTime(xml.schDepart!!, serverDate)
        val expectedDepartureDateTime = parseTime(xml.expDepart!!, serverDate)

        val isStarting = scheduledArrivalDateTime == midnight && expectedArrivalDateTime == midnight
        val isTerminating = scheduledDepartureDateTime == midnight && expectedDepartureDateTime == midnight

        return LiveTime(
            waitTimeMinutes = when {
                isStarting -> minutesBetween(serverDateTime, expectedDepartureDateTime)
                isTerminating -> minutesBetween(serverDateTime, expectedArrivalDateTime)
                else -> minutesBetween(serverDateTime, expectedArrivalDateTime)
            },
            lateTimeMinutes = when {
                isStarting -> minutesBetween(scheduledDepartureDateTime, expectedDepartureDateTime)
                isTerminating -> minutesBetween(scheduledArrivalDateTime, expectedArrivalDateTime)
                else -> minutesBetween(scheduledArrivalDateTime, expectedArrivalDateTime)
            },
            currentTimestamp = serverDateTime.toIso8601(),
            scheduledArrivalTimestamp = if (isStarting) null else scheduledArrivalDateTime.toIso8601(),
            expectedArrivalTimestamp = if (isStarting) null else expectedArrivalDateTime.toIso8601(),
            scheduledDepartureTimestamp = if (isTerminating) null else scheduledDepartureDateTime.toIso8601(),
            expectedDepartureTimestamp = if (isTerminating) null else expectedDepartureDateTime.toIso8601()
        )
    }

    private fun parseTime(timestamp: String, date: LocalDate): ZonedDateTime {
        return LocalTime.parse(timestamp).atDate(date).atZone(dublin)
    }

    private fun minutesBetween(start: ZonedDateTime, end: ZonedDateTime): Int {
        val minutes = Duration.between(start, end).toMinutes().toInt()
        return if (minutes > -1) {
            minutes
        } else {
            val adjustedEnd = end.plusDays(1)
            val adjustedMinutes = Duration.between(start, adjustedEnd).toMinutes().toInt()
            adjustedMinutes
        }
    }

}
