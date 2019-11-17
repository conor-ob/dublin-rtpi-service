package io.rtpi.service.luas

import io.reactivex.Single
import io.rtpi.api.LiveTime
import io.rtpi.api.LuasLiveData
import io.rtpi.api.Operator
import io.rtpi.ktx.validate
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson
import java.util.Objects

abstract class AbstractLuasLiveDataService(private val rtpiApi: RtpiApi) {

    fun getLiveData(stopId: String): Single<List<LuasLiveData>> {
        return rtpiApi.realTimeBusInformation(stopId = stopId, operator = "luas", format = "json")
            .map { response ->
                val liveData = response.results
                    .map { json ->
                        LuasLiveData(
                            liveTimes = listOf(createDueTime(json)),
                            operator = Operator.parse(json.operator!!),
                            route = mapRoute(json),
                            direction = json.direction!!,
                            destination = json.destination!!.replace("LUAS ", "")
                        )
                    }
                    .sortedBy { it.liveTimes.first().waitTimeSeconds }

                val condensedLiveData = LinkedHashMap<Int, LuasLiveData>()
                for (data in liveData) {
                    val id = Objects.hash(data.operator, data.route, data.destination, data.direction)
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
                condensedLiveData.values.toList()
            }
    }

    private fun mapRoute(json: RtpiRealTimeBusInformationJson): String {
        val route = json.route!!.trim()
        if (route.contains("Line")) {
            return route
        }
        return "$route Line"
    }

    protected abstract fun createDueTime(json: RtpiRealTimeBusInformationJson): LiveTime

}
