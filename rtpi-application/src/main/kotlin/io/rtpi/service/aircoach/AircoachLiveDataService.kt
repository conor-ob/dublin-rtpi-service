package io.rtpi.service.aircoach

import io.rtpi.api.LiveTime
import io.rtpi.resource.aircoach.AircoachApi
import io.rtpi.resource.aircoach.EtaJson
import io.rtpi.resource.aircoach.ServiceJson
import io.rtpi.resource.aircoach.TimestampJson
import io.rtpi.time.DateTimeProvider
import io.rtpi.time.toIso8601
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class AircoachLiveDataService(aircoachApi: AircoachApi) : AbstractAircoachLiveDataService<LocalTime>(aircoachApi) {

    override fun createDueTime(expected: EtaJson?, scheduled: TimestampJson): LiveTime {
        val currentTime = DateTimeProvider.getCurrentDateTime()
        val expectedTimestamp = expected?.etaArrive?.dateTime ?: scheduled.dateTime
        val expectedTime = DateTimeProvider.getDateTime(
            expectedTimestamp,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        )
        val waitTimeSeconds = ChronoUnit.SECONDS.between(currentTime, expectedTime).toInt()
        return LiveTime(waitTimeMinutes = waitTimeSeconds, expectedArrivalTimestamp = expectedTime.toIso8601())
    }

    override fun createDueTime(json: ServiceJson): LiveTime {
        val currentDateTime = DateTimeProvider.getCurrentDateTime()
        TODO()
    }

}
