package io.rtpi.service.aircoach

import io.rtpi.api.LiveTime
import io.rtpi.external.aircoach.AircoachApi
import io.rtpi.external.aircoach.EtaJson
import io.rtpi.external.aircoach.TimestampJson
import io.rtpi.time.DateTimeProvider
import io.rtpi.time.toIso8601
import org.threeten.bp.Duration
import org.threeten.bp.format.DateTimeFormatter

private const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)

class AircoachLiveDataService(aircoachApi: AircoachApi) : AbstractAircoachLiveDataService(aircoachApi) {

    override fun createDueTime(expected: EtaJson?, scheduled: TimestampJson): LiveTime {
        val currentTime = DateTimeProvider.getCurrentDateTime()
        val expectedTimestamp = expected?.etaArrive?.dateTime ?: scheduled.dateTime!!
        val expectedTime = DateTimeProvider.getDateTime(
            timestamp = expectedTimestamp,
            formatter = DATE_TIME_FORMATTER
        )
        val scheduledTime = DateTimeProvider.getDateTime(
            timestamp = scheduled.dateTime!!,
            formatter = DATE_TIME_FORMATTER
        )
        return LiveTime(
            waitTimeMinutes = Duration.between(currentTime, expectedTime).toMinutes().toInt(),
            currentTimestamp = currentTime.toIso8601(),
            expectedTimestamp = expectedTime.toIso8601(),
            scheduledTimestamp = scheduledTime.toIso8601()
        )
    }

}