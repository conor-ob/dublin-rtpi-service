package io.rtpi.service.aircoach

import io.reactivex.Single
import io.rtpi.api.AircoachLiveData
import io.rtpi.api.LiveTime
import io.rtpi.external.aircoach.AircoachApi
import io.rtpi.external.aircoach.EtaJson
import io.rtpi.external.aircoach.TimestampJson
import io.rtpi.time.DateTimeProvider
import io.rtpi.time.toIso8601
import java.time.Duration
import java.time.format.DateTimeFormatter

private const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)

class AircoachLiveDataService(private val aircoachApi: AircoachApi) {

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
    private fun createDueTime(expected: EtaJson?, scheduled: TimestampJson): LiveTime {
        val currentTime = DateTimeProvider.getCurrentDateTime()
        val expectedTimestamp = expected?.etaArrive?.dateTime ?: scheduled.dateTime!!
        val expectedTime = DateTimeProvider.getDateTime(
            timestamp = expectedTimestamp,
            formatter = DATE_TIME_FORMATTER
        )
        val scheduledTime = DateTimeProvider.getDateTime(
            timestamp = scheduled.dateTime!!,
            formatter = DATE_TIME_FORMATTER
        )
        return LiveTime(
            waitTimeMinutes = Duration.between(currentTime, expectedTime).toMinutes().toInt(),
            currentTimestamp = currentTime.toIso8601(),
            expectedTimestamp = expectedTime.toIso8601(),
            scheduledTimestamp = scheduledTime.toIso8601()
        )
    }

}
