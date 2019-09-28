package ie.dublin.rtpi.service.luas

import ie.dublin.rtpi.api.DueTime
import ie.dublin.rtpi.api.LuasLiveData
import ie.dublin.rtpi.api.Operator
import ie.dublin.rtpi.ktx.validate
import ie.dublin.rtpi.resource.rtpi.RtpiApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LuasLiveDataService(private val rtpiService: RtpiApi) {

    fun getLiveData(stopId: String, compact: Boolean): List<LuasLiveData> {
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
