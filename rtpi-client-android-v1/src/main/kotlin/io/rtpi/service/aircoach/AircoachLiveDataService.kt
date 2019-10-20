package io.rtpi.service.aircoach

import io.rtpi.api.LiveTime
import io.rtpi.resource.aircoach.AircoachApi
import io.rtpi.resource.aircoach.EtaJson
import io.rtpi.resource.aircoach.TimestampJson
import io.rtpi.time.DateTimeProvider
import io.rtpi.time.toIso8601
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit

class AircoachLiveDataService(aircoachApi: AircoachApi) : AbstractAircoachLiveDataService<LocalTime>(aircoachApi) {

    override fun createDueTime(expected: EtaJson?, scheduled: TimestampJson): LiveTime {
        val currentTime = DateTimeProvider.getCurrentDateTime()
        val expectedTimestamp = expected?.etaArrive?.dateTime ?: scheduled.dateTime
        val expectedTime = DateTimeProvider.getDateTime(
            expectedTimestamp,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        )
        val waitTimeSeconds = ChronoUnit.SECONDS.between(currentTime, expectedTime).toInt()
        return LiveTime(waitTimeSeconds, expectedTime.toIso8601())
    }

}
