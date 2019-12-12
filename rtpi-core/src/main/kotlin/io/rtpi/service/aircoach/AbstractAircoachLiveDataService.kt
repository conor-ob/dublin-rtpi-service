package io.rtpi.service.aircoach

import io.reactivex.Single
import io.rtpi.api.AircoachLiveData
import io.rtpi.api.LiveTime
import io.rtpi.external.aircoach.AircoachApi
import io.rtpi.external.aircoach.EtaJson
import io.rtpi.external.aircoach.TimestampJson

abstract class AbstractAircoachLiveDataService<T>(private val aircoachApi: AircoachApi) {

    fun getLiveData(stopId: String): Single<List<AircoachLiveData>> {
        return aircoachApi.getLiveData(
            id = stopId
        ).map { response ->
            response.services!!
                .filter { json ->
//                    json.eta != null
                    json.time != null
                        && json.time!!.arrive != null
                        && json.time!!.arrive!!.dateTime != null
                        && json.route != null
                        && json.arrival != null
                        && json.depart != null
                        && json.dir != null
                }.map { json ->
                    AircoachLiveData(
                        liveTime = createDueTime(json.eta, json.time!!.arrive!!),
                        route = json.route!!.trim(),
                        destination = json.arrival!!.trim(),
                        origin = json.depart!!.trim(),
                        direction = json.dir!!.trim()
                    )
                }
                .filter { it.liveTime.waitTimeMinutes >= 0 }
                .sortedBy { it.liveTime.waitTimeMinutes }
        }
    }

    // TODO check nullable
    protected abstract fun createDueTime(expected: EtaJson?, scheduled: TimestampJson): LiveTime

}
