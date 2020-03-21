package io.rtpi.time

import io.rtpi.api.LiveTime
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson
import java.time.Duration
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
            currentDateTime = currentTime,
            waitTime = parseDueTime(json),
            expectedDateTime = expectedTime,
            scheduledDateTime = scheduledTime
        )
    }

    private fun parseDueTime(json: RtpiRealTimeBusInformationJson): Duration {
        if (DUE == json.dueTime?.trim()) {
            return Duration.ZERO
        }
        return Duration.ofMinutes(requireNotNull(json.dueTime).toLong())
    }
}
