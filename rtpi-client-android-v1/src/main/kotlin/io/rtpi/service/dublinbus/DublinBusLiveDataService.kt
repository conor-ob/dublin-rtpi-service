package io.rtpi.service.dublinbus

import io.rtpi.api.LiveTime
import io.rtpi.resource.dublinbus.DublinBusApi
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataXml
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.time.DateTimeProvider
import io.rtpi.time.toIso8601
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit

class DublinBusLiveDataService(
    dublinBusApi: DublinBusApi,
    rtpiApi: RtpiApi
) : AbstractDublinBusLiveDataService(dublinBusApi, rtpiApi) {

    override fun createDueTime(xml: DublinBusRealTimeStopDataXml): LiveTime {
        val currentTime = DateTimeProvider.getCurrentDateTime()
        val expectedTimestamp = xml.expectedTimestamp!!
        val expectedTime = DateTimeProvider.getDateTime(
            expectedTimestamp,
            DateTimeFormatter.ISO_DATE_TIME
        )
        val waitTimeSeconds = ChronoUnit.SECONDS.between(currentTime, expectedTime).toInt()
        return LiveTime(waitTimeSeconds, expectedTime.toIso8601())
    }

    override fun createDueTime(json: RtpiRealTimeBusInformationJson): LiveTime {
        val currentTime = DateTimeProvider.getCurrentDateTime()
        val expectedTimestamp = json.arrivalDateTime!!
        val expectedTime = DateTimeProvider.getDateTime(
            expectedTimestamp,
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        )
        val waitTimeSeconds = ChronoUnit.SECONDS.between(currentTime, expectedTime).toInt()
        return LiveTime(waitTimeSeconds, expectedTime.toIso8601())
    }

}
