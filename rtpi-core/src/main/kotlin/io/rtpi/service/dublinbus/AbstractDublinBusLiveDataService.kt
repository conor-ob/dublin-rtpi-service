package io.rtpi.service.dublinbus

import io.rtpi.api.DublinBusLiveData
import io.rtpi.api.LiveTime
import io.rtpi.api.Operator
import io.rtpi.ktx.validate
import io.rtpi.resource.dublinbus.DublinBusApi
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataRequestBodyXml
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataRequestRootXml
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataRequestXml
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataXml
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.Objects

abstract class AbstractDublinBusLiveDataService(
    private val dublinBusApi: DublinBusApi,
    private val rtpiApi: RtpiApi
) {

    fun getLiveData(stopId: String): List<DublinBusLiveData> = runBlocking {
        val dublinBusResponse = async { getDublinBusLiveData(stopId) }
        val rtpiResponse = async { getGoAheadLiveData(stopId) }
        val liveData = dublinBusResponse.await()
            .plus(rtpiResponse.await())
            .sortedBy { it.liveTimes.first().waitTimeSeconds }

        val condensedLiveData = LinkedHashMap<Int, DublinBusLiveData>()
        for (data in liveData) {
            val id = Objects.hash(data.operator, data.route, data.destination)
            var cachedLiveData = condensedLiveData[id]
            if (cachedLiveData == null) {
                condensedLiveData[id] = data
            } else {
                val dueTimes = cachedLiveData.liveTimes.toMutableList()
                dueTimes.add(data.liveTimes.first())
                cachedLiveData = cachedLiveData.copy(liveTimes = dueTimes)
                condensedLiveData[id] = cachedLiveData
            }
        }
        return@runBlocking condensedLiveData.values.toList()
    }

    private suspend fun getDublinBusLiveData(stopId: String): List<DublinBusLiveData> {
        val requestRoot = DublinBusRealTimeStopDataRequestRootXml(stopId, true.toString())
        val requestBody = DublinBusRealTimeStopDataRequestBodyXml(requestRoot)
        val request = DublinBusRealTimeStopDataRequestXml(requestBody)
        return dublinBusApi.getRealTimeStopData(request)
            .validate()
            .dublinBusRealTimeStopData
            .filter { it.routeId != null
                && it.destination != null
                && it.expectedTimestamp != null
            }.map { xml ->
                return@map DublinBusLiveData(
                    liveTimes = listOf(createDueTime(xml)),
                    operator = Operator.DUBLIN_BUS, //TODO
                    route = xml.routeId!!,
                    destination = xml.destination!!
                )
            }
    }
    private suspend fun getGoAheadLiveData(stopId: String): List<DublinBusLiveData> {
        return rtpiApi.realTimeBusInformation(stopId, Operator.GO_AHEAD.shortName, "json")
            .validate()
            .results
            .filter {
                it.route != null
                    && it.destination != null
                    && it.operator != null
                    && it.arrivalDateTime != null
            }.map { json ->
                DublinBusLiveData(
                    liveTimes = listOf(createDueTime(json)),
                    operator = Operator.parse(json.operator!!),
                    route = json.route!!,
                    destination = json.destination!!
                )
            }
    }

    protected abstract fun createDueTime(xml: DublinBusRealTimeStopDataXml): LiveTime

    protected abstract fun createDueTime(json: RtpiRealTimeBusInformationJson): LiveTime

}
