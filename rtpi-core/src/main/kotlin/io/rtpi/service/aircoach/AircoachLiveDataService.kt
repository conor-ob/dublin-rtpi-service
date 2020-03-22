package io.rtpi.service.aircoach

import com.google.inject.Inject
import io.reactivex.Single
import io.rtpi.api.AircoachLiveData
import io.rtpi.api.LiveTime
import io.rtpi.external.aircoach.AircoachApi
import io.rtpi.external.aircoach.EtaJson
import io.rtpi.external.aircoach.ServiceJson
import io.rtpi.external.aircoach.ServiceResponseJson
import io.rtpi.external.aircoach.TimestampJson
import io.rtpi.time.DateTimeProvider
import io.rtpi.validation.validate
import io.rtpi.validation.validateObjects
import java.time.Duration
import java.time.format.DateTimeFormatter

private const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)

class AircoachLiveDataService @Inject constructor(private val aircoachApi: AircoachApi) {

    fun getLiveData(stopId: String): Single<List<AircoachLiveData>> {
        return aircoachApi.getLiveData(id = stopId)
            .map { validateResponse(it) }
    }

    private fun validateResponse(response: ServiceResponseJson): List<AircoachLiveData> {
        if (response.services.isNullOrEmpty()) {
            return emptyList()
        } else {
            return requireNotNull(response.services)
                .filter { json ->
                    validateObjects(json.time, json.time?.arrive, json.time?.arrive?.dateTime, json.route, json.dir)
                }
                .map { json ->
                    AircoachLiveData(
                        liveTime = createDueTime(json.eta, requireNotNull(json.time?.arrive)),
                        route = json.route.validate(),
                        destination = getDestination(json),
                        origin = getOrigin(json),
                        direction = json.dir.validate()
                    )
                }
                .filter { !it.liveTime.waitTime.isNegative }
                .sortedBy { it.liveTime.waitTime }
        }
    }

    private fun getOrigin(json: ServiceJson): String {
        if (json.stops.isNullOrEmpty()) {
            return if (json.depart != null) {
                json.depart.validate()
            } else {
                "Unknown Origin"
            }
        }
        return json.stops.validate().first()
    }

    private fun getDestination(json: ServiceJson): String {
        if (json.stops.isNullOrEmpty()) {
            return if (json.arrival != null) {
                json.arrival.validate()
            } else {
                "Unknown Destination"
            }
        }
        return json.stops.validate().last()
    }

    // TODO check nullable
    private fun createDueTime(expected: EtaJson?, scheduled: TimestampJson): LiveTime {
        val currentTime = DateTimeProvider.getCurrentDateTime()
        val expectedTimestamp = expected?.etaArrive?.dateTime ?: requireNotNull(scheduled.dateTime)
        val expectedTime = DateTimeProvider.getDateTime(
            timestamp = expectedTimestamp,
            formatter = DATE_TIME_FORMATTER
        )
        val scheduledTime = DateTimeProvider.getDateTime(
            timestamp = requireNotNull(scheduled.dateTime),
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
