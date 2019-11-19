package io.rtpi.service.aircoach

import io.rtpi.api.AircoachLiveData
import io.rtpi.api.LiveTime
import io.rtpi.ktx.validate
import io.rtpi.resource.aircoach.AircoachApi
import io.rtpi.resource.aircoach.EtaJson
import io.rtpi.resource.aircoach.ServiceJson
import io.rtpi.resource.aircoach.TimestampJson

abstract class AbstractAircoachLiveDataService<T>(private val aircoachApi: AircoachApi) {

    fun getLiveData(stopId: String): List<AircoachLiveData> {
        return aircoachApi.getLiveData(stopId)
            .validate()
            .services
            .map { json ->
                AircoachLiveData(
                    liveTime = createDueTime(json),
                    route = json.route,
                    destination = json.arrival,
                    origin = json.depart,
                    direction = json.dir
                )
            }
            .filter { it.liveTime.waitTimeMinutes > -1 }
            .sortedBy { it.liveTime.waitTimeMinutes }
//
//        val condensedLiveData = LinkedHashMap<Int, AircoachLiveData>()
//        for (data in liveData) {
//            val id = Objects.hash(data.operator, data.route, data.destination)
//            var cachedLiveData = condensedLiveData[id]
//            if (cachedLiveData == null) {
//                condensedLiveData[id] = data
//            } else {
//                val dueTimes = cachedLiveData.liveTimes.toMutableList()
//                dueTimes.add(data.liveTimes.first())
//                cachedLiveData = cachedLiveData.copy(liveTimes = dueTimes)
//                condensedLiveData[id] = cachedLiveData
//            }
//        }
//        return condensedLiveData.values.toList()
    }

    protected abstract fun createDueTime(expected: EtaJson?, scheduled: TimestampJson): LiveTime

    protected abstract fun createDueTime(json: ServiceJson): LiveTime
}
