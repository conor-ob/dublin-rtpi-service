package io.rtpi.service.aircoach

import com.google.inject.Inject
import io.reactivex.Single
import io.rtpi.api.LiveData
import io.rtpi.api.Operator
import io.rtpi.api.Prediction
import io.rtpi.api.PredictionLiveData
import io.rtpi.api.RouteInfo
import io.rtpi.api.Service
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

    fun getLiveData(stopId: String): Single<List<LiveData>> {
        return aircoachApi.getLiveData(id = stopId)
            .map { validateResponse(it) }
    }

    private fun validateResponse(response: ServiceResponseJson): List<LiveData> {
        if (response.services.isNullOrEmpty()) {
            return emptyList()
        } else {
            return requireNotNull(response.services)
                .filter { json ->
                    validateObjects(json.time, json.time?.arrive, json.time?.arrive?.dateTime, json.route, json.dir)
                }
                .map { json ->
                    PredictionLiveData(
                        prediction = createDueTime(json.eta, requireNotNull(json.time?.arrive)),
                        routeInfo = RouteInfo(
                            route = json.route.validate(),
                            destination = getDestination(json),
                            origin = getOrigin(json),
                            direction = json.dir.validate()
                        ),
                        operator = Operator.AIRCOACH,
                        service = Service.AIRCOACH
                    )
                }
                .filter { !it.prediction.waitTime.isNegative }
                .sortedBy { it.prediction.waitTime }
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
        val stopNames = json.stops.validate().map { it.stop }
        return if (stopNames.isNullOrEmpty()) {
            "Unknown Origin"
        } else {
            return stopNames.first().validate()
        }
    }

    private fun getDestination(json: ServiceJson): String {
        if (json.stops.isNullOrEmpty()) {
            return if (json.arrival != null) {
                json.arrival.validate()
            } else {
                "Unknown Destination"
            }
        }
        val stopNames = json.stops.validate().map { it.stop }
        return if (stopNames.isNullOrEmpty()) {
            "Unknown Origin"
        } else {
            return stopNames.last().validate()
        }
    }

    // TODO check nullable
    private fun createDueTime(expected: EtaJson?, scheduled: TimestampJson): Prediction {
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
        return Prediction(
            waitTime = Duration.between(currentTime, expectedTime),
            currentDateTime = currentTime,
            expectedDateTime = expectedTime,
            scheduledDateTime = scheduledTime
        )
    }
}
