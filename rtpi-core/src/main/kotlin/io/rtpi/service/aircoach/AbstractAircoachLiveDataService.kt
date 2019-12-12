package io.rtpi.service.aircoach

import io.reactivex.Single
import io.rtpi.api.AircoachLiveData
import io.rtpi.api.LiveTime
import io.rtpi.external.aircoach.AircoachApi
import io.rtpi.external.aircoach.EtaJson
import io.rtpi.external.aircoach.ServiceJson
import io.rtpi.external.aircoach.TimestampJson

abstract class AbstractAircoachLiveDataService(private val aircoachApi: AircoachApi) {

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
//                        && json.arrival != null
//                        && json.depart != null
                        && json.dir != null
                }.map { json ->
                    AircoachLiveData(
                        liveTime = createDueTime(json.eta, json.time!!.arrive!!),
                        route = json.route!!.trim(),
                        destination = getDestination(json),
                        origin = getOrigin(json),
                        direction = json.dir!!.trim()
                    )
                }
                .filter { it.liveTime.waitTimeMinutes >= 0 }
                .sortedBy { it.liveTime.waitTimeMinutes }
        }
    }

    private fun getOrigin(json: ServiceJson): String {
        if (json.stops.isNullOrEmpty()) {
            return json.depart!!.trim()
        }
        return json.stops!!.first()
    }

    private fun getDestination(json: ServiceJson): String {
        if (json.stops.isNullOrEmpty()) {
            return json.arrival!!.trim()
        }
        return json.stops!!.last()
    }

    // TODO check nullable
    protected abstract fun createDueTime(expected: EtaJson?, scheduled: TimestampJson): LiveTime

}
