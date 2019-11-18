package io.rtpi.service.buseireann

import io.rtpi.api.LiveTime
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.time.DateTimeProvider
import io.rtpi.time.toIso8601
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit

class BusEireannLiveDataService(rtpiApi: RtpiApi) : AbstractBusEireannLiveDataService(rtpiApi) {

    override fun createDueTime(json: RtpiRealTimeBusInformationJson): LiveTime {
        val currentTime = DateTimeProvider.getCurrentDateTime()
        val expectedTimestamp = json.arrivalDateTime!!
        val expectedTime = DateTimeProvider.getDateTime(
            expectedTimestamp,
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        )
        val waitTimeSeconds = ChronoUnit.SECONDS.between(currentTime, expectedTime).toInt()
        return LiveTime(waitTimeMinutes = waitTimeSeconds, expectedArrivalTimestamp = expectedTime.toIso8601())
    }

}
