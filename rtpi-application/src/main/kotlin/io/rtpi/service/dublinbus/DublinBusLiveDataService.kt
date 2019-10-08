package io.rtpi.service.dublinbus

import io.rtpi.api.Time
import io.rtpi.resource.dublinbus.DublinBusApi
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataXml
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class DublinBusLiveDataService(
    dublinBusApi: DublinBusApi,
    rtpiApi: RtpiApi
) : AbstractDublinBusLiveDataService(dublinBusApi, rtpiApi) {

    override fun createDueTime(xml: DublinBusRealTimeStopDataXml): Time {
        val expected = LocalDateTime.parse(xml.expectedTimestamp!!, DateTimeFormatter.ISO_DATE_TIME)
        val current = LocalDateTime.parse(xml.responseTimestamp!!, DateTimeFormatter.ISO_DATE_TIME)
        val minutes = ChronoUnit.MINUTES.between(current, expected).toInt()
        return Time(minutes)
//        return Time(minutes, expected.toLocalTime())
    }

    override fun createDueTime(json: RtpiRealTimeBusInformationJson): Time {
        return Time(
            if (json.dueTime == "Due") 0 else json.dueTime!!.toInt()
//            LocalDateTime.parse(json.arrivalDateTime!!, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).toLocalTime()
        )
    }

}
