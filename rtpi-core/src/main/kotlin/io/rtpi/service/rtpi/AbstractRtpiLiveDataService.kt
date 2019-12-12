package io.rtpi.service.rtpi

import io.reactivex.Single
import io.rtpi.api.LiveTime
import io.rtpi.api.TimedLiveData
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.time.DateTimeProvider
import io.rtpi.time.toIso8601
import java.time.format.DateTimeFormatter

private const val JSON = "json"
private const val DUE = "Due"
private const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss"
private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)

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

    protected fun createDueTime(serverTimestamp: String, json: RtpiRealTimeBusInformationJson): LiveTime {
        val currentTime = DateTimeProvider.getDateTime(
            timestamp = serverTimestamp,
            formatter = DATE_TIME_FORMATTER
        )
        val expectedTime = DateTimeProvider.getDateTime(
            timestamp = json.arrivalDateTime!!,
            formatter = DATE_TIME_FORMATTER
        )
        val scheduledTime = DateTimeProvider.getDateTime(
            timestamp = json.scheduledArrivalDateTime!!,
            formatter = DATE_TIME_FORMATTER
        )
        return LiveTime(
            currentTimestamp = currentTime.toIso8601(),
            waitTimeMinutes = parseDueTime(json),
            expectedTimestamp = expectedTime.toIso8601(),
            scheduledTimestamp = scheduledTime.toIso8601()
        )
    }

    private fun parseDueTime(json: RtpiRealTimeBusInformationJson): Int {
        if (DUE == json.dueTime?.trim()) {
            return 0
        }
        return json.dueTime!!.toInt()
    }

    protected abstract fun newLiveDataInstance(timestamp: String, json: RtpiRealTimeBusInformationJson): T

}
