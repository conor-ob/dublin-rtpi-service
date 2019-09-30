package io.rtpi.service.luas

import io.rtpi.api.DueTime
import io.rtpi.api.LuasLiveData
import io.rtpi.api.Operator
import io.rtpi.ktx.validate
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson

abstract class AbstractLuasLiveDataService<T>(private val rtpiApi: RtpiApi) {

    fun getLiveData(stopId: String, compact: Boolean): List<LuasLiveData<T>> {
        return rtpiApi.realTimeBusInformation(stopId = stopId, operator = "luas", format = "json")
            .validate()
            .results
            .map { json ->
                LuasLiveData(
                    nextDueTime = createDueTime(json),
                    laterDueTimes = emptyList(),
                    operator = Operator.parse(json.operator!!),
                    route = json.route!!,
                    direction = json.direction!!,
                    destination = json.destination!!.replace("LUAS ", "")
                )
            }
    }

    protected abstract fun createDueTime(json: RtpiRealTimeBusInformationJson): DueTime<T>

}
