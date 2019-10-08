package io.rtpi.service.aircoach

import io.rtpi.api.AircoachLiveData
import io.rtpi.api.Time
import io.rtpi.ktx.validate
import io.rtpi.resource.aircoach.AircoachApi
import io.rtpi.resource.aircoach.EtaJson
import io.rtpi.resource.aircoach.TimestampJson
import java.util.Objects

abstract class AbstractAircoachLiveDataService<T>(private val aircoachApi: AircoachApi) {

    fun getLiveData(stopId: String): List<AircoachLiveData> {
        val liveData = aircoachApi.getLiveData(stopId)
            .validate()
            .services
            .map { json ->
                AircoachLiveData(
                    times = listOf(createDueTime(json.eta, json.time.arrive)),
                    route = json.route,
                    destination = json.arrival
                )
            }
            .filter { it.times.first().minutes > -1 }
            .sortedBy { it.times.first().minutes }

        val condensedLiveData = LinkedHashMap<Int, AircoachLiveData>()
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

    protected abstract fun createDueTime(expected: EtaJson?, scheduled: TimestampJson): Time
}
