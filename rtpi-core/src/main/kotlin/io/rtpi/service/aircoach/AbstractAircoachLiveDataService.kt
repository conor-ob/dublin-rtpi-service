package io.rtpi.service.aircoach

import io.rtpi.api.AircoachLiveData
import io.rtpi.api.DueTime
import io.rtpi.ktx.validate
import io.rtpi.resource.aircoach.AircoachApi
import io.rtpi.resource.aircoach.EtaJson
import io.rtpi.resource.aircoach.TimestampJson

abstract class AbstractAircoachLiveDataService<T>(private val aircoachApi: AircoachApi) {

    fun getLiveData(stopId: String, compact: Boolean): List<AircoachLiveData<T>> {
        return aircoachApi.getLiveData(stopId)
            .validate()
            .services
            .map { json ->
                AircoachLiveData(
                    nextDueTime = createDueTime(json.eta, json.time.arrive),
                    laterDueTimes = emptyList(),
                    route = json.route,
                    destination = json.arrival
                )
            }
            .filter { it.nextDueTime.minutes > -1 }
            .sortedBy { it.nextDueTime.minutes }
    }

    protected abstract fun createDueTime(expected: EtaJson?, scheduled: TimestampJson): DueTime<T>
}
