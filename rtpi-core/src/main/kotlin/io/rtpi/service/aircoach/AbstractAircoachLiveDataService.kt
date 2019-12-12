package io.rtpi.service.aircoach

import io.reactivex.Single
import io.rtpi.api.AircoachLiveData
import io.rtpi.api.LiveTime
import io.rtpi.external.aircoach.AircoachApi
import io.rtpi.external.aircoach.EtaJson
import io.rtpi.external.aircoach.TimestampJson

abstract class AbstractAircoachLiveDataService<T>(private val aircoachApi: AircoachApi) {

    fun getLiveData(stopId: String): Single<List<AircoachLiveData>> {
        return aircoachApi.getLiveData(stopId)
            .map {
                it.services
                    .map { json ->
                        AircoachLiveData(
                            liveTime = createDueTime(json.eta, json.time.arrive),
                            route = json.route,
                            destination = json.arrival,
                            origin = json.depart,
                            direction = json.dir
                        )
                    }
                    .filter { it.liveTime.waitTimeMinutes > -1 }
                    .sortedBy { it.liveTime.waitTimeMinutes }
            }
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

}
