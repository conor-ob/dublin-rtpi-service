package io.rtpi.service.luas

import io.rtpi.api.Time
import io.rtpi.api.LuasLiveData
import io.rtpi.api.Operator
import io.rtpi.ktx.validate
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson
import java.util.Objects

abstract class AbstractLuasLiveDataService<T>(private val rtpiApi: RtpiApi) {

    fun getLiveData(stopId: String): List<LuasLiveData<T>> {
        val liveData = rtpiApi.realTimeBusInformation(stopId = stopId, operator = "luas", format = "json")
            .validate()
            .results
            .map { json ->
                LuasLiveData(
                    times = listOf(createDueTime(json)),
                    operator = Operator.parse(json.operator!!),
                    route = json.route!!,
                    direction = json.direction!!,
                    destination = json.destination!!.replace("LUAS ", "")
                )
            }
            .sortedBy { it.times.first().minutes }

        val condensedLiveData = LinkedHashMap<Int, LuasLiveData<T>>()
        for (data in liveData) {
            val id = Objects.hash(data.operator, data.route, data.destination, data.direction)
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

    protected abstract fun createDueTime(json: RtpiRealTimeBusInformationJson): Time<T>

}
