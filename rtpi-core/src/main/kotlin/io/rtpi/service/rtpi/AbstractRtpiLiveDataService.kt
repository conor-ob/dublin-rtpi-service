package io.rtpi.service.rtpi

import io.reactivex.Single
import io.rtpi.api.LiveTime
import io.rtpi.api.TimedLiveData
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

abstract class AbstractRtpiLiveDataService<T : TimedLiveData>(
    private val rtpiApi: RtpiApi,
    private val operator: String
) {

    fun getLiveData(stopId: String): Single<List<T>> {
        return rtpiApi.realTimeBusInformation(stopId = stopId, operator = operator, format = JSON)
            .map { validateResponse(it) }
    }

    private fun validateResponse(response: RtpiRealTimeBusInformationResponseJson): List<T> {
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
                    newLiveDataInstance(response.timestamp.validate(), json)
                }
                .filter { !it.liveTime.waitTime.isNegative }
                .sortedBy { it.liveTime.waitTime }
        }
    }

    protected abstract fun newLiveDataInstance(timestamp: String, json: RtpiRealTimeBusInformationJson): T

    protected fun createDueTime(serverTimestamp: String, json: RtpiRealTimeBusInformationJson): LiveTime {
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
        return LiveTime(
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
