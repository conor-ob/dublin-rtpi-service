package io.rtpi.service.irishrail

import com.google.inject.Inject
import io.rtpi.api.LiveTime
import io.rtpi.external.irishrail.IrishRailApi
import io.rtpi.external.irishrail.IrishRailStationDataXml
import io.rtpi.time.toIso8601
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val dublin = ZoneId.of("Europe/Dublin")

class IrishRailLiveDataService @Inject constructor(irishRailApi: IrishRailApi) : AbstractIrishRailLiveDataService(irishRailApi) {

    override fun createDueTime(xml: IrishRailStationDataXml): LiveTime {
        val serverDateTime = LocalDateTime.parse(xml.serverTime, DateTimeFormatter.ISO_DATE_TIME).atZone(dublin)
        val serverDate = serverDateTime.toLocalDate()

        val midnight = LocalTime.of(0, 0)
        val scheduledArrivalDateTime = parseTime(xml.schArrival!!, serverDate, serverDateTime)
        val expectedArrivalDateTime = parseTime(xml.expArrival!!, serverDate, serverDateTime)
        val scheduledDepartureDateTime = parseTime(xml.schDepart!!, serverDate, serverDateTime)
        val expectedDepartureDateTime = parseTime(xml.expDepart!!, serverDate, serverDateTime)

        val isStarting = scheduledArrivalDateTime.toLocalTime() == midnight && expectedArrivalDateTime.toLocalTime() == midnight
        val isTerminating = scheduledDepartureDateTime.toLocalTime() == midnight && expectedDepartureDateTime.toLocalTime() == midnight

        return LiveTime(
            waitTimeMinutes = xml.dueIn!!.toInt(),
            currentTimestamp = serverDateTime.toIso8601(),
            expectedTimestamp = if (isTerminating) expectedArrivalDateTime.toIso8601() else expectedDepartureDateTime.toIso8601(),
            scheduledTimestamp = if (isTerminating) scheduledArrivalDateTime.toIso8601() else scheduledDepartureDateTime.toIso8601()
        )
    }

    private fun parseTime(timestamp: String, date: LocalDate, serverDateTime: ZonedDateTime): ZonedDateTime {
        val dateTime = LocalTime.parse(timestamp).atDate(date).atZone(dublin)
        if (Duration.between(dateTime, serverDateTime).toHours() > 12) {
            return dateTime.plusDays(1)
        }
        return dateTime
    }

}
