package io.rtpi.service.luas

import io.rtpi.api.DueTime
import io.rtpi.api.LuasLiveData
import io.rtpi.api.Operator
import io.rtpi.ktx.validate
import io.rtpi.resource.rtpi.RtpiApi
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LuasLiveDataService(private val rtpiService: RtpiApi) {

    fun getLiveData(stopId: String, compact: Boolean): List<LuasLiveData<LocalTime>> {
        return rtpiService.realTimeBusInformation(stopId = stopId, operator = "luas", format = "json")
            .validate()
            .results
            .map { json ->
                LuasLiveData(
                    nextDueTime = DueTime(
                        if (json.dueTime == "Due") 0 else json.dueTime!!.toInt(),
                        LocalDateTime.parse(json.arrivalDateTime!!, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).toLocalTime()
                    ),
                    laterDueTimes = emptyList(),
                    operator = Operator.parse(json.operator!!),
                    route = json.route!!,
                    direction = json.direction!!,
                    destination = json.destination!!.replace("LUAS ", "")
                )
            }
    }

}
