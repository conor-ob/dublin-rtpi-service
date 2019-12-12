package io.rtpi.service.luas

import io.reactivex.Single
import io.rtpi.api.LiveTime
import io.rtpi.api.LuasLiveData
import io.rtpi.api.Operator
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson

abstract class AbstractLuasLiveDataService(private val rtpiApi: RtpiApi) {

    fun getLiveData(stopId: String): Single<List<LuasLiveData>> {
        return rtpiApi.realTimeBusInformation(
            stopId = stopId,
            operator = "luas",
            format = "json"
        ).map { response ->
            response.results
                .filter { json ->
                    json.route != null
                        && json.operator != null
                        && json.destination != null
                        && json.arrivalDateTime != null
                        && json.scheduledArrivalDateTime != null
                        && json.origin != null
                        && json.direction != null
                }.map { json ->
                    LuasLiveData(
                        liveTime = createDueTime(response.timestamp!!, json),
                        operator = Operator.parse(json.operator!!.trim()),
                        route = json.route!!.trim(),
                        destination = json.destination!!.replace("LUAS ", "").trim(),
                        direction = json.direction!!.trim(),
                        origin = json.origin!!.replace("LUAS ", "").trim()
                    )
                }
                .filter { it.liveTime.waitTimeMinutes >= 0 }
                .sortedBy { it.liveTime.waitTimeMinutes }
        }
    }

    protected abstract fun createDueTime(serverTimestamp: String, json: RtpiRealTimeBusInformationJson): LiveTime

}
