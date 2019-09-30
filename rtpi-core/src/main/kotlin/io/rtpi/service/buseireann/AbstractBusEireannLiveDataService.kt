package io.rtpi.service.buseireann

import io.rtpi.api.BusEireannLiveData
import io.rtpi.api.DueTime
import io.rtpi.api.Operator
import io.rtpi.ktx.validate
import io.rtpi.resource.rtpi.RtpiApi
import io.rtpi.resource.rtpi.RtpiRealTimeBusInformationJson

abstract class AbstractBusEireannLiveDataService<T>(private val rtpiService: RtpiApi) {

    fun getLiveData(stopId: String, compact: Boolean): List<BusEireannLiveData<T>> {
        return rtpiService.realTimeBusInformation(stopId = stopId, operator = "be", format = "json")
            .validate()
            .results
            .map { json ->
                BusEireannLiveData(
                    nextDueTime = createDueTime(json),
                    laterDueTimes = emptyList(),
                    operator = Operator.parse(json.operator!!),
                    route = json.route!!,
                    destination = json.destination!!.replace("LUAS ", "")
                )
            }
    }

    protected abstract fun createDueTime(json: RtpiRealTimeBusInformationJson): DueTime<T>
}
