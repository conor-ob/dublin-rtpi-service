package io.rtpi.service.dublinbus

import io.rtpi.api.DublinBusLiveData
import io.rtpi.api.DueTime
import io.rtpi.api.Operator
import io.rtpi.ktx.validate
import io.rtpi.resource.dublinbus.DublinBusApi
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataRequestBodyXml
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataRequestRootXml
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataRequestXml
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataXml
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson

abstract class AbstractDublinBusLiveDataService<T>(
    private val dublinBusApi: DublinBusApi,
    private val rtpiApi: RtpiApi
) {

    fun getLiveData(stopId: String, compact: Boolean): List<DublinBusLiveData<T>> {
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
                return@map DublinBusLiveData(
                    nextDueTime = createDueTime(xml),
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
                    nextDueTime = createDueTime(json),
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

    protected abstract fun createDueTime(xml: DublinBusRealTimeStopDataXml): DueTime<T>

    protected abstract fun createDueTime(json: RtpiRealTimeBusInformationJson): DueTime<T>

}
