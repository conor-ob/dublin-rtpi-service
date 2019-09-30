package io.rtpi.service.luas

import io.rtpi.api.DueTime
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

class LuasLiveDataService(rtpiApi: RtpiApi) : AbstractLuasLiveDataService<LocalTime>(rtpiApi) {

    override fun createDueTime(json: RtpiRealTimeBusInformationJson): DueTime<LocalTime> {
        return DueTime(
            if (json.dueTime == "Due") 0 else json.dueTime!!.toInt(),
            LocalDateTime.parse(json.arrivalDateTime!!, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).toLocalTime()
        )
    }

}
