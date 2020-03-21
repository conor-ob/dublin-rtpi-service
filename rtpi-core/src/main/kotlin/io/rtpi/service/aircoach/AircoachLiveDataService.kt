package io.rtpi.service.aircoach

import com.google.inject.Inject
import io.reactivex.Single
import io.rtpi.api.AircoachLiveData
import io.rtpi.api.LiveTime
import io.rtpi.external.aircoach.AircoachApi
import io.rtpi.external.aircoach.EtaJson
import io.rtpi.external.aircoach.ServiceJson
import io.rtpi.external.aircoach.TimestampJson
import io.rtpi.time.DateTimeProvider
import java.time.Duration
import java.time.format.DateTimeFormatter

private const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)

class AircoachLiveDataService @Inject constructor(private val aircoachApi: AircoachApi) {

    fun getLiveData(stopId: String): Single<List<AircoachLiveData>> {
        return aircoachApi.getLiveData(
            id = stopId
        ).map { response ->
            response.services
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
                .filter { it.liveTime.waitTime.isPositive() }
                .sortedBy { it.liveTime.waitTime }
        }
    }

    private fun getOrigin(json: ServiceJson): String {
        if (json.stops.isNullOrEmpty()) {
            return json.depart!!.trim()
        }
        return json.stops.first()
    }

    private fun getDestination(json: ServiceJson): String {
        if (json.stops.isNullOrEmpty()) {
            return json.arrival!!.trim()
        }
        return json.stops.last()
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
            waitTime = Duration.between(currentTime, expectedTime),
            currentDateTime = currentTime,
            expectedDateTime = expectedTime,
            scheduledDateTime = scheduledTime
        )
    }

}

fun Duration.isPositive(): Boolean = !isNegative && !isZero
