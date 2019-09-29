package ie.dublin.rtpi.service.buseireann

import ie.dublin.rtpi.api.BusEireannLiveData
import ie.dublin.rtpi.api.DueTime
import ie.dublin.rtpi.api.Operator
import ie.dublin.rtpi.ktx.validate
import ie.dublin.rtpi.resource.rtpi.RtpiApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BusEireannLiveDataService(private val rtpiService: RtpiApi) {

    fun getLiveData(stopId: String, compact: Boolean): List<BusEireannLiveData> {
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
