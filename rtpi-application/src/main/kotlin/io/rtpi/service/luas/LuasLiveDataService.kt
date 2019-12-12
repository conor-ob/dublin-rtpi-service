package io.rtpi.service.luas

import io.rtpi.api.LiveTime
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.time.toIso8601
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val dublin = ZoneId.of("Europe/Dublin")

class LuasLiveDataService(rtpiApi: RtpiApi) : AbstractLuasLiveDataService(rtpiApi) {

    override fun createDueTime(serverTimestamp: String, json: RtpiRealTimeBusInformationJson): LiveTime {
        return LiveTime(
            currentTimestamp = parseDateTime(serverTimestamp).toIso8601(),
            waitTimeMinutes = parseDueTime(json),
            expectedTimestamp = parseDateTime(json.arrivalDateTime!!).toIso8601(),
            scheduledTimestamp = parseDateTime(json.scheduledArrivalDateTime!!).toIso8601()
        )
    }

    private fun parseDateTime(timestamp: String): ZonedDateTime {
        return LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).atZone(dublin)
    }

}
