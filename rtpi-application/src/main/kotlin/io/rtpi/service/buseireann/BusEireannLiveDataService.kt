package io.rtpi.service.buseireann

import io.rtpi.api.BusEireannLiveData
import io.rtpi.api.DueTime
import io.rtpi.api.Operator
import io.rtpi.ktx.validate
import io.rtpi.resource.rtpi.RtpiApi
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class BusEireannLiveDataService(private val rtpiService: RtpiApi) {

    fun getLiveData(stopId: String, compact: Boolean): List<BusEireannLiveData<LocalTime>> {
        return rtpiService.realTimeBusInformation(stopId = stopId, operator = "be", format = "json")
            .validate()
            .results
            .map { json ->
                    BusEireannLiveData(
                        nextDueTime = DueTime(
                            if (json.dueTime == "Due") 0 else json.dueTime!!.toInt(),
                            LocalDateTime.parse(json.arrivalDateTime!!, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).toLocalTime()
                        ),
                        laterDueTimes = emptyList(),
                        operator = Operator.parse(json.operator!!),
                        route = json.route!!,
                        destination = json.destination!!.replace("LUAS ", "")
                    )
            }
    }

}
