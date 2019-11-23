package io.rtpi.service.aircoach

import io.rtpi.api.LiveTime
import io.rtpi.external.aircoach.AircoachApi
import io.rtpi.external.aircoach.EtaJson
import io.rtpi.external.aircoach.TimestampJson
import io.rtpi.time.DateTimeProvider
import io.rtpi.time.toIso8601
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AircoachLiveDataService(aircoachApi: AircoachApi) : AbstractAircoachLiveDataService<LocalTime>(aircoachApi) {

    override fun createDueTime(expected: EtaJson?, scheduled: TimestampJson): LiveTime {
        val currentTime = DateTimeProvider.getCurrentDateTime()
        val expectedTimestamp = expected?.etaArrive?.dateTime ?: scheduled.dateTime
        val expectedTime = DateTimeProvider.getDateTime(
            expectedTimestamp,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        )
        val scheduledTime = DateTimeProvider.getDateTime(
            scheduled.dateTime,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        )
        return LiveTime(
            waitTimeMinutes = Duration.between(currentTime, expectedTime).toMinutes().toInt(),
            currentTimestamp = currentTime.toIso8601(),
            expectedTimestamp = expectedTime.toIso8601(),
            scheduledTimestamp = scheduledTime.toIso8601()
        )
    }

}
