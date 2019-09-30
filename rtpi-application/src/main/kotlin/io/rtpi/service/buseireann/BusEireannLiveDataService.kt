package io.rtpi.service.buseireann

import io.rtpi.api.DueTime
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class BusEireannLiveDataService(rtpiApi: RtpiApi) : AbstractBusEireannLiveDataService<LocalTime>(rtpiApi) {

    override fun createDueTime(json: RtpiRealTimeBusInformationJson): DueTime<LocalTime> {
        return DueTime(
            if (json.dueTime == "Due") 0 else json.dueTime!!.toInt(),
            LocalDateTime.parse(json.arrivalDateTime!!, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).toLocalTime()
        )
    }

}
