package io.rtpi.service.buseireann

import io.rtpi.api.Time
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

class BusEireannLiveDataService(rtpiApi: RtpiApi) : AbstractBusEireannLiveDataService<LocalTime>(rtpiApi) {

    override fun createDueTime(json: RtpiRealTimeBusInformationJson): Time<LocalTime> {
        return Time(
            if (json.dueTime == "Due") 0 else json.dueTime!!.toInt(),
            LocalDateTime.parse(json.arrivalDateTime!!, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).toLocalTime()
        )
    }

}
