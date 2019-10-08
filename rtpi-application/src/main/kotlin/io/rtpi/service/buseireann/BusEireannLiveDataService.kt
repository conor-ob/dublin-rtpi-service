package io.rtpi.service.buseireann

import io.rtpi.api.Time
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class BusEireannLiveDataService(rtpiApi: RtpiApi) : AbstractBusEireannLiveDataService(rtpiApi) {

    override fun createDueTime(json: RtpiRealTimeBusInformationJson): Time {
        return Time(
            if (json.dueTime == "Due") 0 else json.dueTime!!.toInt()
//            LocalDateTime.parse(json.arrivalDateTime!!, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).toLocalTime()
        )
    }

}
