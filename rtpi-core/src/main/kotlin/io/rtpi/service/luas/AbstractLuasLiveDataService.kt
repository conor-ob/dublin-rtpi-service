package io.rtpi.service.luas

import io.reactivex.Single
import io.rtpi.api.LiveTime
import io.rtpi.api.LuasLiveData
import io.rtpi.api.Operator
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson

abstract class AbstractLuasLiveDataService(private val rtpiApi: RtpiApi) {

    fun getLiveData(stopId: String): Single<List<LuasLiveData>> {
        return rtpiApi.realTimeBusInformation(stopId = stopId, operator = "luas", format = "json")
            .map { response ->
                val liveData = response.results
                    .map { json ->
                        LuasLiveData(
                            liveTime = createDueTime(response.timestamp!!, json),
                            operator = Operator.parse(json.operator!!),
                            route = json.route!!,
                            direction = json.direction!!,
                            destination = json.destination!!.replace("LUAS ", ""),
                            origin = json.origin!!
                        )
                    }
                    .sortedBy { it.liveTime.waitTimeMinutes }
                liveData

//                val condensedLiveData = LinkedHashMap<Int, LuasLiveData>()
//                for (data in liveData) {
//                    val id = Objects.hash(data.operator, data.route, data.destination, data.direction)
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

}
