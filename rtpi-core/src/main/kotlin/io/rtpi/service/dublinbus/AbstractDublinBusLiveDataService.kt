package io.rtpi.service.dublinbus

import io.rtpi.api.DublinBusLiveData
import io.rtpi.api.Time
import io.rtpi.api.Operator
import io.rtpi.ktx.validate
import io.rtpi.resource.dublinbus.DublinBusApi
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataRequestBodyXml
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataRequestRootXml
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataRequestXml
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataXml
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson
import java.util.Objects

abstract class AbstractDublinBusLiveDataService<T>(
    private val dublinBusApi: DublinBusApi,
    private val rtpiApi: RtpiApi
) {

    fun getLiveData(stopId: String): List<DublinBusLiveData<T>> {
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
                    times = listOf(createDueTime(xml)),
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
                    times = listOf(createDueTime(json)),
                    operator = Operator.parse(json.operator!!),
                    route = json.route!!,
                    destination = json.destination!!
                )
            }

        val liveData = dublinBusFiltered
            .plus(rtpiFiltered)
            .sortedBy { it.times.first().minutes }

        val condensedLiveData = LinkedHashMap<Int, DublinBusLiveData<T>>()
        for (data in liveData) {
            val id = Objects.hash(data.operator, data.route, data.destination)
            var cachedLiveData = condensedLiveData[id]
            if (cachedLiveData == null) {
                condensedLiveData[id] = data
            } else {
                val dueTimes = cachedLiveData.times.toMutableList()
                dueTimes.add(data.times.first())
                cachedLiveData = cachedLiveData.copy(times = dueTimes)
                condensedLiveData[id] = cachedLiveData
            }
        }
        return condensedLiveData.values.toList()
    }

    protected abstract fun createDueTime(xml: DublinBusRealTimeStopDataXml): Time<T>

    protected abstract fun createDueTime(json: RtpiRealTimeBusInformationJson): Time<T>

}
