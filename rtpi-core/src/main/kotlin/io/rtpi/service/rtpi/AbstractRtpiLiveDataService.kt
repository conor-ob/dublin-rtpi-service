package io.rtpi.service.rtpi

import io.reactivex.Single
import io.rtpi.api.LiveTime
import io.rtpi.api.TimedLiveData
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson

private const val JSON = "json"

abstract class AbstractRtpiLiveDataService<T : TimedLiveData>(
    private val rtpiApi: RtpiApi,
    private val operator: String
) {

    fun getLiveData(stopId: String): Single<List<T>> {
        return rtpiApi.realTimeBusInformation(
            stopId = stopId,
            operator = operator,
            format = JSON
        ).map { response ->
            response.results!!
                .filter { json ->
                    json.route != null
                        && json.operator != null
                        && json.destination != null
                        && json.arrivalDateTime != null
                        && json.scheduledArrivalDateTime != null
                        && json.origin != null
                        && json.direction != null
                }
                .map { json -> newLiveDataInstance(response.timestamp!!, json) }
                .filter { it.liveTime.waitTimeMinutes >= 0 }
                .sortedBy { it.liveTime.waitTimeMinutes }
        }
    }

    protected abstract fun newLiveDataInstance(timestamp: String, json: RtpiRealTimeBusInformationJson): T

    protected abstract fun createDueTime(serverTimestamp: String, json: RtpiRealTimeBusInformationJson): LiveTime

}
