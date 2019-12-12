package io.rtpi.service

import io.rtpi.api.LiveTime
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.time.DateTimeProvider
import io.rtpi.time.toIso8601
import java.time.format.DateTimeFormatter

private const val DUE = "Due"
private const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss"
private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)

object RtpiLiveTimeFactory {

    fun createLiveTime(serverTimestamp: String, json: RtpiRealTimeBusInformationJson): LiveTime {
        val currentTime = DateTimeProvider.getDateTime(
            timestamp = serverTimestamp,
            formatter = DATE_TIME_FORMATTER
        )
        val expectedTime = DateTimeProvider.getDateTime(
            timestamp = json.arrivalDateTime!!,
            formatter = DATE_TIME_FORMATTER
        )
        val scheduledTime = DateTimeProvider.getDateTime(
            timestamp = json.scheduledArrivalDateTime!!,
            formatter = DATE_TIME_FORMATTER
        )
        return LiveTime(
            currentTimestamp = currentTime.toIso8601(),
            waitTimeMinutes = parseDueTime(json),
            expectedTimestamp = expectedTime.toIso8601(),
            scheduledTimestamp = scheduledTime.toIso8601()
        )
    }

    private fun parseDueTime(json: RtpiRealTimeBusInformationJson): Int {
        if (DUE == json.dueTime?.trim()) {
            return 0
        }
        return json.dueTime!!.toInt()
    }

}
