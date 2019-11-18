package io.rtpi.service.dublinbus

import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.rtpi.api.DublinBusLiveData
import io.rtpi.api.LiveTime
import io.rtpi.api.Operator
import io.rtpi.resource.dublinbus.DublinBusApi
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataRequestBodyXml
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataRequestRootXml
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataRequestXml
import io.rtpi.resource.dublinbus.DublinBusRealTimeStopDataXml
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson

abstract class AbstractDublinBusLiveDataService(
    private val dublinBusApi: DublinBusApi,
    private val rtpiApi: RtpiApi
) {

    fun getLiveData(stopId: String): Single<List<DublinBusLiveData>> {
        return Single.zip(
            getDublinBusLiveData(stopId),
            getGoAheadLiveData(stopId),
            BiFunction { t1, t2 -> aggregate(t1, t2) }
        )
    }

    private fun aggregate(t1: List<DublinBusLiveData>, t2: List<DublinBusLiveData>): List<DublinBusLiveData> {
        return t1.plus(t2).sortedBy { it.liveTime.waitTimeMinutes }

//        val condensedLiveData = LinkedHashMap<Int, DublinBusLiveData>()
//        for (data in liveData) {
//            val id = Objects.hash(data.operator, data.route, data.destination)
//            var cachedLiveData = condensedLiveData[id]
//            if (cachedLiveData == null) {
//                condensedLiveData[id] = data
//            } else {
//                val dueTimes = cachedLiveData.liveTime.toMutableList()
//                dueTimes.add(data.liveTime.first())
//                cachedLiveData = cachedLiveData.copy(liveTime = dueTimes)
//                condensedLiveData[id] = cachedLiveData
//            }
//        }
//        return condensedLiveData.values.toList()
    }

    private fun getDublinBusLiveData(stopId: String): Single<List<DublinBusLiveData>> {
        val requestRoot = DublinBusRealTimeStopDataRequestRootXml(stopId, true.toString())
        val requestBody = DublinBusRealTimeStopDataRequestBodyXml(requestRoot)
        val request = DublinBusRealTimeStopDataRequestXml(requestBody)
        return dublinBusApi.getRealTimeStopData(request)
            .map { response ->
                response.dublinBusRealTimeStopData
                .filter { it.routeId != null
                    && it.destination != null
                    && it.expectedTimestamp != null
                }.map { xml ->
                    DublinBusLiveData(
                        liveTime = createDueTime(xml),
                        operator = Operator.DUBLIN_BUS, //TODO
                        route = xml.routeId!!,
                        destination = xml.destination!!,
                        direction = "",
                        origin = ""
                    )
                }
            }
    }
    private fun getGoAheadLiveData(stopId: String): Single<List<DublinBusLiveData>> {
        return rtpiApi.realTimeBusInformation(stopId, Operator.GO_AHEAD.shortName, "json")
            .map { response ->
                response.results.filter {
                    it.route != null
                        && it.destination != null
                        && it.operator != null
                        && it.arrivalDateTime != null
                }.map { json ->
                    DublinBusLiveData(
                        liveTime = createDueTime(json),
                        operator = Operator.parse(json.operator!!),
                        route = json.route!!,
                        destination = json.destination!!,
                        origin = json.origin!!,
                        direction = json.direction!!
                    )
                }
            }
    }

    protected abstract fun createDueTime(xml: DublinBusRealTimeStopDataXml): LiveTime

    protected abstract fun createDueTime(json: RtpiRealTimeBusInformationJson): LiveTime

}
