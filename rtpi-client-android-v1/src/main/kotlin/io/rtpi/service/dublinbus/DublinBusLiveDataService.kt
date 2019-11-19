package io.rtpi.service.dublinbus

import io.rtpi.api.LiveTime
import io.rtpi.resource.dublinbus.DublinBusApi
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.time.toIso8601
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

private val dublin = ZoneId.of("Europe/Dublin")

class DublinBusLiveDataService(
    dublinBusApi: DublinBusApi,
    rtpiApi: RtpiApi
) : AbstractDublinBusLiveDataService(dublinBusApi, rtpiApi) {

    override fun createDueTime(serverTimestamp: String, json: RtpiRealTimeBusInformationJson): LiveTime {
        return LiveTime(
            currentTimestamp = parseDateTime(serverTimestamp).toIso8601(),
            waitTimeMinutes = json.dueTime!!.toInt(),
            expectedTimestamp = parseDateTime(json.arrivalDateTime!!).toIso8601(),
            scheduledTimestamp = parseDateTime(json.scheduledArrivalDateTime!!).toIso8601()
        )
    }

    private fun parseDateTime(timestamp: String): ZonedDateTime {
        return LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).atZone(dublin)
    }

}
