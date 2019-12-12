package io.rtpi.service.buseireann

import io.reactivex.Single
import io.rtpi.api.BusEireannLiveData
import io.rtpi.api.LiveTime
import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson

abstract class AbstractBusEireannLiveDataService(private val rtpiService: RtpiApi) {

    fun getLiveData(stopId: String): Single<List<BusEireannLiveData>> {
        return rtpiService.realTimeBusInformation(stopId = stopId, operator = "be", format = "json")
            .map { response ->
                val liveData = response.results
                    .map { json ->
                        BusEireannLiveData(
                            liveTime = createDueTime(response.timestamp!!, json),
                            operator = Operator.parse(json.operator!!),
                            route = json.route!!,
                            destination = json.destination!!,
                            origin = json.origin!!,
                            direction = json.direction!!
                        )
                    }
                    .sortedBy { it.liveTime.waitTimeMinutes }
                    liveData
//                val condensedLiveData = LinkedHashMap<Int, BusEireannLiveData>()
//                for (data in liveData) {
//                    val id = Objects.hash(data.operator, data.route, data.destination)
//                    var cachedLiveData = condensedLiveData[id]
//                    if (cachedLiveData == null) {
//                        condensedLiveData[id] = data
//                    } else {
//                        val dueTimes = cachedLiveData.liveTime.toMutableList()
//                        dueTimes.add(data.liveTime.first())
//                        cachedLiveData = cachedLiveData.copy(liveTime = dueTimes)
//                        condensedLiveData[id] = cachedLiveData
//                    }
//                }
//                condensedLiveData.values.toList()
            }
    }

    protected abstract fun createDueTime(serverTimestamp: String, json: RtpiRealTimeBusInformationJson): LiveTime

    protected fun parseDueTime(json: RtpiRealTimeBusInformationJson): Int {
        if ("Due" == json.dueTime) {
            return 0
        }
        return json.dueTime!!.toInt()
    }
}
