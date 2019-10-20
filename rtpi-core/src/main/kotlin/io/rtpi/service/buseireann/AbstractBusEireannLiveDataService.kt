package io.rtpi.service.buseireann

import io.rtpi.api.BusEireannLiveData
import io.rtpi.api.LiveTime
import io.rtpi.api.Operator
import io.rtpi.ktx.validate
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson
import java.util.Objects

abstract class AbstractBusEireannLiveDataService(private val rtpiService: RtpiApi) {

    fun getLiveData(stopId: String): List<BusEireannLiveData> {
        val liveData = rtpiService.realTimeBusInformation(stopId = stopId, operator = "be", format = "json")
            .validate()
            .results
            .map { json ->
                BusEireannLiveData(
                    liveTimes = listOf(createDueTime(json)),
                    operator = Operator.parse(json.operator!!),
                    route = json.route!!,
                    destination = json.destination!!.replace("LUAS ", "")
                )
            }
            .sortedBy { it.liveTimes.first().waitTimeSeconds }

        val condensedLiveData = LinkedHashMap<Int, BusEireannLiveData>()
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
        return condensedLiveData.values.toList()
    }

    protected abstract fun createDueTime(json: RtpiRealTimeBusInformationJson): LiveTime
}
