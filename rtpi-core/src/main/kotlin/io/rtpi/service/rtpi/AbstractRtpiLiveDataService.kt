package io.rtpi.service.rtpi

import io.reactivex.Single
import io.rtpi.api.LiveData
import io.rtpi.api.Operator
import io.rtpi.api.Prediction
import io.rtpi.api.PredictionLiveData
import io.rtpi.api.RouteInfo
import io.rtpi.api.Service
import io.rtpi.external.rtpi.RtpiApi
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationJson
import io.rtpi.external.rtpi.RtpiRealTimeBusInformationResponseJson
import io.rtpi.time.DateTimeProvider
import io.rtpi.validation.validate
import io.rtpi.validation.validateStrings
import java.time.Duration
import java.time.format.DateTimeFormatter

private const val JSON = "json"
private const val DUE = "Due"
private const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss"
private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)

abstract class AbstractRtpiLiveDataService(
    private val rtpiApi: RtpiApi,
    private val operator: String
) {

    fun getLiveData(stopId: String): Single<List<LiveData>> {
        return rtpiApi.realTimeBusInformation(stopId = stopId, operator = operator, format = JSON)
            .map { validateResponse(it) }
    }

    private fun validateResponse(response: RtpiRealTimeBusInformationResponseJson): List<PredictionLiveData> {
        return if (response.results.isNullOrEmpty()) {
            emptyList()
        } else {
            requireNotNull(response.results)
                .filter { json ->
                    validateStrings(
                        response.timestamp, json.route, json.operator, json.direction, json.arrivalDateTime,
                        json.scheduledArrivalDateTime, json.origin, json.destination
                    )
                }
                .map { json ->
                    PredictionLiveData(
                        prediction = createDueTime(response.timestamp.validate(), json),
                        operator = Operator.parse(json.operator.validate()),
                        service = Service.LUAS,
                        routeInfo = RouteInfo(
                            route = json.route.validate(),
                            destination = json.destination.validate().replace("LUAS", "").validate(),
                            direction = json.direction.validate(),
                            origin = json.origin.validate().replace("LUAS", "").validate()
                        )
                    )
                }
                .filter { !it.prediction.waitTime.isNegative }
                .sortedBy { it.prediction.waitTime }
        }
    }

    private fun createDueTime(serverTimestamp: String, json: RtpiRealTimeBusInformationJson): Prediction {
        val currentTime = DateTimeProvider.getDateTime(
            timestamp = serverTimestamp,
            formatter = DATE_TIME_FORMATTER
        )
        val expectedTime = DateTimeProvider.getDateTime(
            timestamp = json.arrivalDateTime.validate(),
            formatter = DATE_TIME_FORMATTER
        )
        val scheduledTime = DateTimeProvider.getDateTime(
            timestamp = json.scheduledArrivalDateTime.validate(),
            formatter = DATE_TIME_FORMATTER
        )
        return Prediction(
            currentDateTime = currentTime,
            waitTime = parseDueTime(json),
            expectedDateTime = expectedTime,
            scheduledDateTime = scheduledTime
        )
    }

    private fun parseDueTime(json: RtpiRealTimeBusInformationJson): Duration {
        if (DUE == json.dueTime?.trim()) {
            return Duration.ZERO
        }
        return Duration.ofMinutes(requireNotNull(json.dueTime).toLong())
    }
}
