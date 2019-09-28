package ie.dublin.rtpi.service.dublinbus

import ie.dublin.rtpi.api.DublinBusLiveData
import ie.dublin.rtpi.api.DueTime
import ie.dublin.rtpi.api.Operator
import ie.dublin.rtpi.ktx.validate
import ie.dublin.rtpi.resource.dublinbus.DublinBusApi
import ie.dublin.rtpi.resource.dublinbus.DublinBusRealTimeStopDataRequestBodyXml
import ie.dublin.rtpi.resource.dublinbus.DublinBusRealTimeStopDataRequestRootXml
import ie.dublin.rtpi.resource.dublinbus.DublinBusRealTimeStopDataRequestXml
import ie.dublin.rtpi.resource.rtpi.RtpiApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class DublinBusLiveDataService(
    private val dublinBusApi: DublinBusApi,
    private val rtpiApi: RtpiApi
) {

    fun getLiveData(stopId: String, compact: Boolean): List<DublinBusLiveData> {
        val requestRoot = DublinBusRealTimeStopDataRequestRootXml(stopId, true.toString())
        val requestBody = DublinBusRealTimeStopDataRequestBodyXml(requestRoot)
        val request = DublinBusRealTimeStopDataRequestXml(requestBody)

        val dublinBusResponse = dublinBusApi.getRealTimeStopData(request).validate()
        val rtpiResponse = rtpiApi.realTimeBusInformation(stopId, Operator.GO_AHEAD.shortName, "json").validate()

        val dublinBusFiltered = dublinBusResponse.dublinBusRealTimeStopData.filter {
            it.routeId != null
                && it.destination != null
                && it.expectedTimestamp != null
        }
            .map { xml ->
                val expected = LocalDateTime.parse(xml.expectedTimestamp!!, DateTimeFormatter.ISO_DATE_TIME)
                val current = LocalDateTime.parse(xml.responseTimestamp!!, DateTimeFormatter.ISO_DATE_TIME)
                val minutes = ChronoUnit.MINUTES.between(current, expected)
                return@map DublinBusLiveData(
                    nextDueTime = DueTime(
                        minutes,
                        expected.toLocalTime()
                    ),
                    laterDueTimes = emptyList(),
                    operator = Operator.DUBLIN_BUS, //TODO
                    route = xml.routeId!!,
                    destination = xml.destination!!
                )
            }

        val rtpiFiltered = rtpiResponse.results.filter {
            it.route != null
                && it.destination != null
                && it.operator != null
                && it.arrivalDateTime != null
        }
            .map { json ->
                DublinBusLiveData(
                    nextDueTime = DueTime(
                        if (json.dueTime == "Due") 0L else json.dueTime!!.toLong(),
                        LocalDateTime.parse(json.arrivalDateTime!!, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")).toLocalTime()
                    ),
                    laterDueTimes = emptyList(),
                    operator = Operator.parse(json.operator!!),
                    route = json.route!!,
                    destination = json.destination!!
                )
            }

        return dublinBusFiltered
            .plus(rtpiFiltered)
            .sortedBy { it.nextDueTime.minutes }
    }

}
